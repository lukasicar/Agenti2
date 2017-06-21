var app = angular.module('choice.controllers', []);
 
app.controller('choiceController', ['$rootScope','$scope','$location','$http','$state','homeService','$uibModal',
  	function ($rootScope, $scope, $location,$http,$state,homeService,$uibModal) {
	
	var init = function () {
        try {
            if (WebSocket) {
                alert("WebSockets supported.");
            }
        } catch (e) {
            alert("WebSockets not supported.");
            $scope.showHttpConsole();
        }
    };



    $scope.showHttpConsole = function () {
    	$rootScope.type = 'HTTP';
        $state.transitionTo('home');
    };


    $scope.showWebSocketConsole = function () {
    	$rootScope.type = 'WEB_SOCKET';
        $state.transitionTo('home');
    };
    
    
    init();

}]);