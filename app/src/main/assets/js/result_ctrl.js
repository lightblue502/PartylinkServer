var audio_result = new Audio("sounds/result.mp3");
audio_result.play();
var app = angular.module("myApp", []);
app.controller('ResultCtrl', ['$scope','$interval','$timeout', function($scope, $interval, $timeout){

  var a1 = new Person(1,"John","img/637.jpg");
  var a2 = new Person(3,"Jane");
  var b1 = new Person(2,"Sam","img/coby.jpg");
  var b2 = new Person(4,"");

  // $scope.showTeamA =[];
  // $scope.showTeamB =[];
  $scope.teamA = [];
  $scope.teamB = [];

  $scope.allPlayers = [];
  // $scope.allPlayers = [a1, a2, b1, b2];
  $scope.conShow = false;
  $timeout(function () {
    $scope.conShow = true;
    $scope.conClass = "animated zoomIn ";

    $timeout(function () {
      $scope.conClass = "animated infinite pulse ";
    },1000);
  },1000);


  //web call android UI Ready;
  Android.onUiReady();


  //MOCK
 //  $scope.resultScores = [
 //  {team:"teamA",gameName:"ShakeGAME",winRoundA:2, winRoundB:0},
	// {team:"teamB",gameName:"Numberic",winRoundA:1, winRoundB:2},
	// {team:"teamB",gameName:"QA",winRoundA:3, winRoundB:1}
 //  ]

 //  var recentGame = $scope.resultScores[$scope.resultScores.length-1];
 //  recentGame.team = recentGame.team == "teamA" ? "RED Win": "BLUE Win";
 //  $scope.recentGame = recentGame; 
  
}]);

function Person(id, name, team, icon) {
	this.id = id;
	this.name = name || "Anonymous";
	this.icon = icon || "img/person.png";
	this.team = team;
}

function stopAudio(){
  console.log("stopAudio");
  audio_result.pause();
  audio_result.currentTime = 0;
}

  



