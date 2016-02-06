var app = angular.module("myApp", []);
app.controller('NumericCtrl', ['$scope','$interval', function($scope, $interval){
  console.log("===== NumericCtrl =====");

  // var a1 = new Person(1,"John",0,"img/637.jpg");
  // var a2 = new Person(3,"Jane",0);
  // var b1 = new Person(2,"Sam",1,"img/coby.jpg");
  // var b2 = new Person(4,"",1);
  $scope.allPlayers=[];
  $scope.teamA= $scope.allPlayers.filter(function(p){return p.team==0;});
  $scope.teamB= $scope.allPlayers.filter(function(p){return p.team==1;});
  $scope.display = "display content center"

  var time;
  $scope.score = {'A':0, 'B':0};
  $scope.title = "Where is My";
  $scope.subtitle = "NUMBER?"
  $scope.displayText = "Ready !!";

  //generate ICON WinRoundA nad WinRoundB = 0
  $scope.iconScore = generateIconScore(3, 0 ,0);

  var playing = function(){
    $scope.viewer = "open";
    // $scope.showStatus = "playing";
    // $scope.countdown = 3;
    // time = $interval( function(){ 
    //   if($scope.countdown <= 0){
    //     $scope.displayTextCSS = "displayText center bigFont"
    //     // $scope.displayText = ans;

    //     if($scope.countdown <= -5){
    //       // $scope.viewer = "close";
    //       $interval.cancel(time);
    //     }

    //   }
      
    //     $scope.countdown--;
    // }, 1000);
  };
  var ready =function(){
    $scope.showStatus = "Ready !!";
  }
  playing();

  //web call android UI Ready;
  Android.onUiReady();

}]);
  
