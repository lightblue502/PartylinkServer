var app = angular.module('PartyApp',[]);

app.controller('BodyController', ['$scope', function($scope) {

	var a1 = new Person(1,"John",0,"img/637.jpg");
	// var a2 = new Person(3,"Jane",0);
	var b1 = new Person(2,"Sam",1,"img/coby.jpg");
	// var b2 = new Person(4,"",1);

	$scope.allPlayers=[a1,b1];
	$scope.teamA= $scope.allPlayers.filter(function(p){return p.team==0;});
	$scope.teamB= $scope.allPlayers.filter(function(p){return p.team==1;});
	$scope.score={"A":0, "B":0};
	$scope.scoreMax = 500;
	$scope.round = 1;

	$scope.showleft = [];
	$scope.showright = [];

	$scope.showleftbox = {'position':'absolute', 'left':(100-40*($scope.showleft.length+1))/2+'%'};
	$scope.showrightbox = {'position':'absolute', 'right':(100-40*($scope.showright.length+1))/2+'%'};
}]);

function shake(personId, team){
	var elem = document.getElementById(personId);
	var show;
	var person = getPerson(personId);
	// if(elem.style.visibility == "hidden")
	// 	elem.style.visibility = "visible";
	// else
	elem.style.visibility = "hidden";

	angular.element(document.body).scope().$apply(
		function($scope){
			if(team == 'A')
				$scope.showleft.push(person);
			else
				$scope.showright.push(person);
			console.log(person);
		});

	// show.innerHTML += elem.innerHTML;
	return true;
}

function Person(id, name, team, icon) {
	this.id = id;
	this.name = name || "Anonymous";
	this.icon = icon || "img/person.png";
	this.team = team;
}

function getPerson(personId){
	var aa;
	angular.element(document.body).scope().$apply(
		function($scope){
			var a = $scope.allPlayers.filter(function(p){return p.id==personId;});
			// console.log(a[0]);
			aa = a[0];
		});
	// console.log(aa);
	return aa;
}