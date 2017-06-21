var app = angular.module('newAgent.controllers', []);
 
app.controller('newAgent', ['$rootScope','$scope','$location','$http','$state','$uibModalInstance',
  	function ($rootScope, $scope, $location,$http,$state,$uibModalInstance) {
	
	$scope.close = function () {

        $uibModalInstance.close();
    

    };
    
    
    $scope.runAgent = function () {
        if (!$scope.name) {
            alert("Ime ne smije biti prazno");
            return;
        }
        $scope.homeService.run($scope.type, $scope.name,
        		function(response){
        			alert(response.data);
        			$scope.close();
        		});
    };
	
}]);