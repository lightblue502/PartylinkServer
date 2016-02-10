var app = angular.module("myApp", []);
app.controller('EndCtrl', ['$scope','$interval', function($scope, $interval){

  // var a1 = new Person(1,"John","img/637.jpg");
  // var a2 = new Person(3,"Jane");
  // var b1 = new Person(2,"Sam","img/coby.jpg");
  // var b2 = new Person(4,"");

  $scope.showTeamA =[];
  $scope.showTeamB =[];
  $scope.teamA = [];
  $scope.teamB = [];

  // $scope.teamWin = "BLUE"

  //web call android UI Ready;
  Android.onUiReady();

}]);

function Person(id, name, team, icon) {
	this.id = id;
	this.name = name || "Anonymous";
	this.icon = icon || "img/person.png";
	this.team = team;
}


  

