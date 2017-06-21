var services = angular.module('webSocket.services', ['ngResource']);

//var baseUrl = 'http://localhost\\:8080';

services.service('webSocketService', ['$http','$location','$log','$rootScope', 
	function($http,$location,$log,$rootScope){
	
	var socket = null;
	var sendQueue=[];
	var successListeners = {};
    var errorListeners = {};
    addSuccessListener = function (type, listener) {
        successListeners[type] = listener;
    };
    addErrorListener = function (type, listener) {
        errorListeners[type] = listener;
    };
    addListenersAndSend = function (type, success, error, data) {
        addSuccessListener(type, success);
        addErrorListener(type, error);
        send(type, data);
    };
    
    
	try {
		alert("otvara socket");
        socket = new WebSocket('ws://' + $location.host() + ":" + $location.port() + '/Project2/socket')
    } catch (e) {
    	alert(e);
        return;
    }
    
    send = function (type, data) {
        var json = JSON.stringify({
            type: type,
            data: JSON.stringify(data)
        });
        if (socket.readyState == socket.CONNECTING) {
            sendQueue.push(json)
        } else {
            socket.send(json);
        }
    };
    
    socket.onmessage = function (event) {
        var object = event.data ? JSON.parse(event.data) : {}, success, error;
        object.data = object.data ? JSON.parse(object.data) : {};
        if (object.success) {
            success = successListeners[object.type];
            if (success) {
                object.status = 200;
                success(object, true);
                $rootScope.$digest();
            }
        	//return object.data;
        } else {
            alert("error  "+object.data)
        }
    };

    socket.onopen = function () {
        $log.info("WebSocket onOpen");
        while (sendQueue.length > 0) {
            socket.send(sendQueue.pop());
        }
    }
    
	
	
	this.getClasses = function(success, error){
		addListenersAndSend('getClasses', success, error);
		//return $http.get("/Project2/rest/agents/classes");
	}
	
	
	this.run = function(type,name,success, error){
		var string = JSON.stringify(type);
		addListenersAndSend('runAgent', success, error, {
            agentType: type,
            name: name
        });
		//return $http.put("/Project2/rest/agents/running/"+string+"/"+name);
	}
	
	this.getRunning = function(success, error){
		alert("adislic");
		addListenersAndSend('getRunning', success, error);
		//return $http.get("/Project2/rest/agents/running");
	}
	
	
	this.stopAgent=function (aid,success, error) {
        //aid = JSON.stringify(aid);
        addListenersAndSend('stopAgent', success, error, aid);
        //return $http.delete("/Project2/rest/agents/running/"+aid);
    },

    this.sendMessage=function(message,success, error){
    	 addListenersAndSend('sendMessage', success, error, message);
    	//return $http.post("/Project2/rest/messages",message);
    }
    
    this.getPerformatives=function(success, error){
    	 addListenersAndSend('getPerformatives', success, error);
    	//return $http.get("/Project2/rest/messages");
    }
	
}]);