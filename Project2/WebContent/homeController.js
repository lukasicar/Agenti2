var app = angular.module('home.controllers', []);
 
app.controller('homeController', ['$rootScope','$scope','$location','$http',
	'$state','homeService','$uibModal','webSocketService',
  	function ($rootScope, $scope, $location,
  			$http,$state,homeService,$uibModal,webSocketService) {
	
	$scope.agentTypes=[];
	$scope.runningAgents = [];
	performatives = [];
	var consoleService = null;
	
	function init() {
		if (!$rootScope.type) {
            $location.path('/');
            return;
        } else {
            consoleService = $rootScope.type === 'HTTP' ? homeService : webSocketService;
        }
		consoleService.getClasses(
				function (response) {
					for(var x in response.data)
						$scope.agentTypes.push(response.data[x]);
				});
		consoleService.getRunning(
				function(response){
					for(var x in response.data)
						$scope.runningAgents.push(response.data[x]);
				});
		consoleService.getPerformatives(
			function(response){
				for(var x in response.data){
					performatives.push(response.data[x]);
				}
			}
		);
    }
	
	init();
	
	$scope.runAgent = function (type) {

        var scope = $scope.$new(true);
        scope.homeService = consoleService;
        scope.type = type;
        scope.scope=$scope;
        
		//alert("POGODJEN");
		
		$uibModal.open({
			animation: true,

            templateUrl: 'newAgent.html',

            controller: 'newAgent',

            scope: scope
		}).closed.then(function(){
			consoleService.getRunning(
					function(response){
						$scope.runningAgents=[];
						for(var x in response.data)
							$scope.runningAgents.push(response.data[x]);
					});
		});
	
    };
    
    
    $scope.stopAgent = function (aid) {
    	consoleService.stopAgent(aid,
            function () {
            	consoleService.getRunning(
						function(response){
							$scope.runningAgents=[];
							for(var x in response.data)
								$scope.runningAgents.push(response.data[x]);
						});
            });
    };
    
    $scope.newMessage = function (aid) {
        var scope = $scope.$new(true);
        scope.homeService = consoleService;
        scope.runningAgents = $scope.runningAgents;
        scope.performatives=performatives;
        scope.message = {
            receivers: aid ? [aid] : []
        };
        $uibModal.open({
            animation: true,
            templateUrl: 'newMessage.html',
            controller: 'newMessage',
            size: 'lg',
            scope: scope,
            backdrop : true
        })
    };
    
}]);