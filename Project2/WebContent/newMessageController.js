var app = angular.module('newMessage.controllers', []);
 
app.controller('newMessage', ['$rootScope','$scope','$location','$http','$state','$uibModalInstance',
  	function ($rootScope, $scope, $location,$http,$state,$uibModalInstance) {
	
	$scope.close = function () {
        $uibModalInstance.close();
    };
    
    $scope.message.performative = "REQUEST";

    $scope.send = function () {
    	//alert($scope.message.receivers);
        if (!$scope.message.performative) {
           alert("You have to choose performative.");
           return;
        } else if (!$scope.message.receivers || !$scope.message.receivers.length) {
            alert("You have to specify at least one receiver.");
        } else {
            //$scope.alertMessage = null;
        }
        $scope.homeService.sendMessage($scope.message,
            function () {
                $scope.close();
            },
            function (response) {
                alert(response.data);
                $scope.close();
            });
    };
}]);