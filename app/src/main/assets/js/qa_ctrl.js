var audio_qa = new Audio("sounds/AlexSkrindoLights.mp3");
audio_qa.loop = true;
audio_qa.play();

var app = angular.module('PartyApp',[]);

app.controller('BodyController', ['$scope','$timeout', function($scope,$timeout) {


	// var a1 = new Person(91,"John",'teamA',"img/637.jpg");
	// var a2 = new Person(3,"Jane",'teamA');
	// var b1 = new Person(92,"Sam",'teamB',"img/coby.jpg");
	$scope.teamA= [];
	$scope.teamB= [];
	$scope.score={"A":"00", "B":"00"};
	$scope.round = 1;

	$scope.show = [];
	$scope.status = "";
	$scope.animate = "animated bounceIn";

	$scope.timeoutFLAG;
	$scope.showAnimate = function() {
		$scope.showClass = "animated zoomIn showAnimated";
		$timeout.cancel($scope.timeoutFLAG);
		$scope.timeoutFLAG = $timeout(function() {
			$scope.showClass = "";
			$scope.answerClass = "";
			$scope.status = $scope.status.split(" ")[0];
		},800);
	}

	Android.onUiReady();
}]);

function stopAudio(){
	console.log("stopAudio");
	audio_qa.pause();
	audio_qa.currentTime = 0;
}

function Person(id, name, team, icon) {
	this.id = id;
	this.name = name || "Anonymous";
    this.icon = icon || "img/person.png";
	this.team = team;
}

function getPerson(personId, team){
	var aa;
	angular.element(document.body).scope().$apply(
		function($scope){
			if(team != null){
				if(team == 'teamA')
					aa = $scope.teamA.filter(function(p){return p.id==personId;})[0];
				else
					aa = $scope.teamB.filter(function(p){return p.id==personId;})[0];
			}
			else{
				aa = $scope.teamA.filter(function(p){return p.id==personId;})[0];
				if(aa == null)
					aa = $scope.teamB.filter(function(p){return p.id==personId;})[0];
			}
		});
	if(aa == null)
		console.log("ERROR : getPerson id:"+personId+" team"+team);
	return aa;
}