var audio_home = new Audio("sounds/DiscoSmurfRumble.mp3");
audio_home.loop = true;
audio_home.play();

var app = angular.module("myApp", []);
app.controller('HomeCtrl', ['$scope','$interval','$timeout', function($scope, $interval, $timeout){
  // 
  
  
  //web call android UI Ready;
  // Android.onUiReady();
  $scope.display = "loading...";

  $scope.timeoutFn = function (fn, duration) {
  	$timeout(fn,duration);
  }

}]);

function Person(id, name, team, icon) {
	this.id = id;
	this.name = name || "Anonymous";
	this.icon = icon || "img/person.png";
	this.team = team;
}

function stopAudio(){
	console.log("stopAudio");
	audio_home.pause();
	audio_home.currentTime = 0;
}
