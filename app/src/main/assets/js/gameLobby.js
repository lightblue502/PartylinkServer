var app = angular.module("myApp", []);
app.controller('GameLobbyCtrl', ['$scope','$interval', function($scope, $interval){

  //web call android UI Ready;
  // Android.onUiReady();
  // var a1 = new Person(1,"John","img/637.jpg");
  // var a2 = new Person(3,"Jane");
  // var b1 = new Person(2,"Sam","img/coby.jpg");
  // var b2 = new Person(4,"");

  $scope.allPlayers=[];

}]);

function Person(id, name, icon) {
  this.id = id;
  this.name = name || "Anonymous";
  this.icon = icon || "img/person.png";
}

function setPlayer(id, name){
  angular.element(document.body).scope().$apply(function($scope){
    $scope.allPlayers.push(new Person(id, name));
  });
}