var services = angular.module('home.services', ['ngResource']);

//var baseUrl = 'http://localhost\\:8080';

services.service('homeService', ['$http', function($http){
	
	this.getClasses = function(success,error){
		 $http({

             method: 'GET',

             url: '/Project2/rest/agents/classes'

         }).then(success, error);
		//return $http.get("/Project2/rest/agents/classes");
	}
	
	
	this.run = function(type,name,success,error){
		var string = JSON.stringify(type);
		 $http({

             method: 'PUT',

             url: '/Project2/rest/agents/running/' + string + '/' + name

         }).then(success, error);
		//return $http.put("/Project2/rest/agents/running/"+string+"/"+name);
	}
	
	this.getRunning = function(success,error){
		$http({

            method: 'GET',

            url: '/Project2/rest/agents/running'

        }).then(success, error);
		//return $http.get("/Project2/rest/agents/running");
	}
	
	
	this.stopAgent=function (aid,success,error) {
        aid = JSON.stringify(aid);
        $http({

            method: 'DELETE',

            url: '/Project2/rest/agents/running/' + aid

        }).then(success, error);
        //return $http.delete("/Project2/rest/agents/running/"+aid);
    },

    this.sendMessage=function(message,success,error){
    	$http({

            method: 'POST',

            url: '/Project2/rest/messages',

            data: message

        }).then(success, error);
    	//return $http.post("/Project2/rest/messages",message);
    }
    
    this.getPerformatives=function(success,error){
    	$http({

            method: 'GET',

            url: '/Project2/rest/messages'

        }).then(success, error);
    	//return $http.get("/Project2/rest/messages");
    }
	
}]);