var app = angular.module("myApp", []);
app.controller('PauseCtrl', ['$scope','$interval', function($scope, $interval){

  //web call android UI Ready;
  Android.onUiReady();
  // $scope.display = "loading...";

}]);

function Person(id, name, team, icon) {
	this.id = id;
	this.name = name || "Anonymous";
	this.icon = icon || "img/person.png";
	this.team = team;
}


  

