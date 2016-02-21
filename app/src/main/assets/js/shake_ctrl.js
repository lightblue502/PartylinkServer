var audio_shake = new Audio("sounds/AlexSkrindoLights.mp3");
audio_shake.play();

var app = angular.module('PartyApp',[]);

app.controller('BodyController', ['$scope','$interval', function($scope,$interval) {

	var a1 = new Person(91,"John",'teamA',"img/637.jpg");
	var a2 = new Person(93,"Jane",'teamA');
	var a3 = new Person(95,"joe",'teamA');
	var b1 = new Person(92,"Sam",'teamB',"img/coby.jpg");
	var b2 = new Person(94,"",'teamB');
	var b3 = new Person(96,"eiei",'teamB');

	// $scope.allPlayers=[a1,b1];
	// $scope.teamA= [a1,a2,a3];
	// $scope.teamB= [b1,b2,b3];
	$scope.teamA= [];
	$scope.teamB= [];
	$scope.realScore={"A":1, "B":1};
	$scope.score={"A":1, "B":1};
	$scope.scoreMax = 500;
	$scope.round = 1;

	$scope.showleft = [];
	$scope.showright = [];

	$scope.shakeClass ={shake:"animated shake"};
	$scope.showStyle = { 'left':{'row':{},'box':{'width':30+'vh'},'name':{}},
	'right':{'row':{},'box':{'width':30+'vh'},'name':{}}};
  	updateStyle($scope);

  	$scope.iconScore = generateIconScore(3, 0 ,0); // 3 is max round

  	$interval(function() {
	    if( Math.abs($scope.score.A - $scope.realScore.A) >= 0.2) {
	      	$scope.score.A += ($scope.realScore.A - $scope.score.A)/10 ;
	    }
	    if( Math.abs($scope.score.B - $scope.realScore.B) >= 0.2) {
	      	$scope.score.B += ($scope.realScore.B - $scope.score.B)/10 ;
	    }
	  }, 10);

	Android.onUiReady();
}]);

function stopAudio(){
	console.log("stopAudio");
	audio_shake.pause();
	audio_shake.currentTime = 0;
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
			if(team == 'teamA')
				aa = $scope.teamA.filter(function(p){return p.id==personId;})[0];
			else
				aa = $scope.teamB.filter(function(p){return p.id==personId;})[0];
		});
	if(aa == null)
		console.log("ERROR : getPerson id:"+personId+" team"+team);
	return aa;
}

function generateIconScore(maxIcon, winRoundA, winRoundB){
  var icons = [
    {
      name :"red", winRound: winRoundA, img:{dark :"img/shake/red_score.png", light:"img/shake/red_score-win.png"}
    },
    {
      name :"blue", winRound: winRoundB, img:{dark :"img/shake/blue_score.png", light:"img/shake/blue_score-win.png"}
    }
  ];
  var data = {};
  for(index in icons){
    var icon = icons[index];
    data[icon.name] = {dark:[],light:[]};
    if(icon.winRound > 0){
      for (var j = 0; j < icon.winRound; j++) {
        data[icon.name].light.push({img:icon.img.light});
      }
    }
    for (var i = 0; i < maxIcon - icon.winRound; i++) {
      data[icon.name].dark.push({img:icon.img.dark});
    }
    
  }
  return data;
}

function updateStyle ($scope) {
	__manageStyle__("left", $scope);
	__manageStyle__("right", $scope);

	function __manageStyle__ (side, $scope) { //left || right

		var width;
		var n =	$scope["show"+side].length;
		if(n == 1) width = 34;
		else if(n == 2) width = 28;
		else if(n == 3) width = 25;

		$scope.showStyle[side].row.width = (width+2)*n +'vh'; //+ margin*2
		$scope.showStyle[side].row.left =  (width+2)*n /2*(-1) +'vh';
		$scope.showStyle[side].row.top =  (n-1)*3 +'vh';
		$scope.showStyle[side].box.width =  width +'vh';
		$scope.showStyle[side].box.height =  width +'vh';
		$scope.showStyle[side].name.width =  width +'vh';
		console.log($scope.showStyle[side]);

	};
}
var stageShake = {};
function shakeAnimate (side, $scope) { //left || right
	if(stageShake[side] != null){
		window.clearTimeout(stageShake[side]);
		console.log("A");
	}
	if(!$scope.shakeClass[side] || $scope.shakeClass[side] == ""){
		$scope.shakeClass[side] = $scope.shakeClass.shake;
		console.log("B");
	}
	else{
		$scope.shakeClass[side] += " infinite";
		console.log("C");
	}
	stageShake[side] = window.setTimeout(function() {
		$scope.shakeClass[side] = "";
		console.log("CLEAR");
	}, 1000);
}
