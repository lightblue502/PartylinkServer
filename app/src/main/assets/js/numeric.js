
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
  $scope.displayCSS = "display center"
  var time
  var number1  = 123;
  var number2 = 432;
  var symbol = "+";
  var ans = number1 + number2;

  $scope.blueScore = 8;
  $scope.redScore = 2;
  $scope.display = number1 + " "+ symbol + " " + number2 + " = ?";

  var playing = function(){
    $scope.viewer = "open";
    $scope.countdown = 3;
    time = $interval( function(){ 
      if($scope.countdown <= 0){
        $scope.displayCSS = "display center bigFont"
        $scope.display = ans;

        if($scope.countdown <= -5){
          $scope.viewer = "close";
          $interval.cancel(time);
        }

      }
      
        $scope.countdown--;
    }, 1000);
  };
  playing();
}]);
