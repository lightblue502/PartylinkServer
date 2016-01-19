
// $(function () {
//   $('#myInput').addClass('open');
//   $('#myInput, #myInput button.close').on('keyup', function(event) {
//     if (event.keyCode == 27 || event.keyCode == 8) {
//       again();
//     }
//   });

//   $('#myInput, #myInput button.close').on('click', function(event) {
//     again()
//   });
//   function again(){
//     $(this).removeClass('open');
//     window.location.replace("/plusNumber/");
//   }

// });

var app = angular.module("myApp", []);
app.controller('NumericCtrl', ['$scope','$interval', function($scope, $interval){
  console.log("hello NumericCtrl");
  $scope.displayCSS = "display center"
  var time;
  $scope.round = 1;
  $scope.blueScore = 0;
  $scope.redScore = 0;
  $scope.display = " 1 + 2 = ? ";
  

  var playing = function(){
    $scope.viewer = "open";
    $scope.showStatus = "playing";
    // $scope.countdown = 3;
    // time = $interval( function(){ 
    //   if($scope.countdown <= 0){
    //     $scope.displayCSS = "display center bigFont"
    //     // $scope.display = ans;

    //     if($scope.countdown <= -5){
    //       // $scope.viewer = "close";
    //       $interval.cancel(time);
    //     }

    //   }
      
    //     $scope.countdown--;
    // }, 1000);
  };
  var ready =function(){
    $scope.showStatus = "ready";
  }
  playing();
}]);

function getCurrentScore(score ,team){
  console.log("hello getCurrentScore");
  angular.element(document.body).scope().$apply(function($scope){
        if(team == 'A'){
          $scope.blueScore = score;
        }else{
          $scope.redScore = score;
        }
  });
  console.log("score :" + score + " ---- "+team);
};

function getQuestion(params){
  console.log("hello getQuestion");
  angular.element(document.body).scope().$apply(function($scope){
    $scope.showStatus = "NEW QUESTION";
    var number1  =  params[0];
    var number2 = params[1];
    var symbol = params[2];
    var ans = params[3];
    $scope.display = number1 + " "+ symbol + " " + number2 + " = "+ans;
  });
};

function getRound(round){
  angular.element(document.body).scope().$apply(function($scope){
    $scope.round = round;
  });
};
  // scoreA , socreB , number1, number2, symbol , ans