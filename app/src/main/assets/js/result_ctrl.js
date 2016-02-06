var app = angular.module("myApp", []);
app.controller('ResultCtrl', ['$scope','$interval', function($scope, $interval){
  //web call android UI Ready;
  Android.onUiReady();

 //  $scope.resultScores = [
 //  {team:"teamA",gameName:"ShakeGAME"},
	// {team:"teamB",gameName:"Numberic"},
	// {team:"teamB",gameName:"QA"}
 //  ]
}]);