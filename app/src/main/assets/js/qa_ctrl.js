var app = angular.module('PartyApp',[]);

app.controller('BodyController', ['$scope', function($scope) {


	// var a1 = new Person(91,"John",'teamA',"img/637.jpg");
	// var a2 = new Person(3,"Jane",0);
	// var b1 = new Person(92,"Sam",'teamB',"img/coby.jpg");
	$scope.teamA= [];
	$scope.teamB= [];
	$scope.score={"A":0, "B":0};
	$scope.round = 1;

	$scope.show = [];

	$scope.showbox = {'position':'absolute', 'left':100-30/2+'vh'};
  	$scope.iconScore = generateIconScore(3, 0 ,0); // 3 is max round


	Android.onUiReady();
}]);

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
      name :"red", winRound: winRoundA, colors:{dark :"#4E0404", light:"#F55"}
    },
    {
      name :"blue", winRound: winRoundB, colors:{dark :"#0B2B46", light:"#5096FF"}
    }
  ];
  var data = {};
  for(index in icons){
    var icon = icons[index];
    data[icon.name] = [];
    if(icon.winRound > 0){
      for (var j = 0; j < icon.winRound; j++) {
        data[icon.name].push({color:icon.colors.light});
      }
    }
    for (var i = 0; i < maxIcon - icon.winRound; i++) {
      data[icon.name].push({color:icon.colors.dark});
    }
    
  }
  return data;
}