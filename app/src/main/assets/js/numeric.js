
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
  $scope.blueScore = 8;
  $scope.redScore = 2;


  var playing = function(){
    $scope.viewer = "open";
    $scope.countdown = 3;
    time = $interval( function(){ 
      if($scope.countdown <= 0){
        $scope.displayCSS = "display center bigFont"
        // $scope.display = ans;

        if($scope.countdown <= -5){
          // $scope.viewer = "close";
          $interval.cancel(time);
        }

      }
      
        $scope.countdown--;
    }, 1000);
  };
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
  })
  console.log("score :" + score + " ---- "+team);
}
function getQuestion(value , params){
  var number1  = "";
  var number2 = "";
  var symbol = "";
  console.log("hello getQuestion");
  angular.element(document.body).scope().$apply(function($scope){
    switch(params) {
      case 'number1':
         number1 = params;
      break;
      case 'number2':
         number2 = params ;
      break;
      case 'symbol':
         symbol = params ;
      break; 
      case 'ans':
         ans = params ;
      break;
    }
    $scope.display = number1 + " "+ symbol + " " + number2 + " = ?";
  });
  console.log(number1 + " "+ symbol + " " + number2 + " = ?");
}
  // scoreA , socreB , number1, number2, symbol , ans