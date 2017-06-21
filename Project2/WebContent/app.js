angular.module('app', ['ui.router','home.services','home.controllers',
	'newAgent.controllers','ui.bootstrap','newMessage.controllers',
	'choice.controllers','oi.select','webSocket.services'
	]).config(function($stateProvider, $urlRouterProvider) {
    
    $urlRouterProvider.otherwise('/choice');
    
    $stateProvider
    
    .state('home', {
    	url : '/home',
      	templateUrl : 'home.html',
      	controller : 'homeController'
     })
     .state('choice',{
    	 url : '/choice',
    	 templateUrl: 'choice.html',
    	 controller: 'choiceController'
     })
     
});