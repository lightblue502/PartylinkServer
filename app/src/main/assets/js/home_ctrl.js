var audio_home = new Audio("sounds/DiscoSmurfRumble.mp3");
audio_home.play();

var app = angular.module("myApp", []);
app.controller('HomeCtrl', ['$scope','$interval','$timeout', function($scope, $interval, $timeout){
  // 
  
  
  //web call android UI Ready;
  Android.onUiReady();
  $scope.display = "loading...";

}]);

function Person(id, name, team, icon) {
	this.id = id;
	this.name = name || "Anonymous";
	this.icon = icon || "img/person.png";
	this.team = team;
}


  

