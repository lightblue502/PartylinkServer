var audio_home = new Audio("sounds/DiscoSmurfRumble.mp3");
audio_home.loop = true;
audio_home.play();

var app = angular.module("myApp", []);
app.controller('GameLobbyCtrl', ['$scope','$interval', function($scope, $interval){


  // var a1 = new Person(91,"John","img/637.jpg");
  // var a2 = new Person(93,"Jane");
  // var a3 = new Person(95,"joe");
  // var b1 = new Person(92,"Sam","img/coby.jpg");
  // var b2 = new Person(94);
  // var b3 = new Person(96,"eiei");

  $scope.allPlayers=[];

  $scope.showStyle = {'row':{},'box':{},'name':{}};
  updateStyle($scope);

  Android.onUiReady();
  
}]);

function Person(id, name, icon) {
  console.log(icon);
  this.id = id;
  this.name = name || "Anonymous";
  this.icon = icon || "img/person.png";
}

function stopAudio(){
  console.log("stopAudio");
  audio_home.pause();
  audio_home.currentTime = 0;
}


function setPlayer(id, name, iconPath){
  console.log(iconPath);
  angular.element(document.body).scope().$apply(function($scope){
    $scope.allPlayers.push(new Person(id, name, iconPath));
    updateStyle($scope);
  });
}

function getCountdown(countdown){
  angular.element(document.body).scope().$apply(function($scope){
    $scope.countdown = countdown;
    $scope.countClass = "";
    setTimeout(function() {
      $scope.$apply(function () {
        $scope.countClass = "animated fadeOut ";
      });
    },200);
  });
}

function updateStyle ($scope) {
  var width = 30;
  var n = $scope.allPlayers.length;

  $scope.showStyle.row.width = (width+6*(6-n))*n +'vh'; //+ margin*2
  $scope.showStyle.row.left =  (width+6*(6-n))*n /2*(-1) +'vh';
  $scope.showStyle.box.width =  width +'vh';
  $scope.showStyle.box.height =  width +'vh';
  $scope.showStyle.box.top =  20; //no vh
  $scope.showStyle.box.margin =  "0 "+ 3*(6-n) +'vh'; //margin 3x
  $scope.showStyle.name.width =  width +'vh';
  console.log($scope.showStyle);

}

