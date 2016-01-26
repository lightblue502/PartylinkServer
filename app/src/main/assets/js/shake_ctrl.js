var app = angular.module('PartyApp',[]);

app.controller('BodyController', ['$scope', function($scope) {

	// var a1 = new Person(91,"John",'teamA',"img/637.jpg");
	// var a2 = new Person(3,"Jane",0);
	// var b1 = new Person(92,"Sam",'teamB',"img/coby.jpg");
	// var b2 = new Person(4,"",1);

	// $scope.allPlayers=[a1,b1];
	$scope.teamA= [];
	$scope.teamB= [];
	$scope.score={"A":0, "B":0};
	$scope.scoreMax = 500;
	$scope.round = 1;

	$scope.showleft = [];
	$scope.showright = [];

	$scope.showleftbox = {'position':'absolute', 'left':(100-40*($scope.showleft.length+1))/2+'%'};
	$scope.showrightbox = {'position':'absolute', 'right':(100-40*($scope.showright.length+1))/2+'%'};

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