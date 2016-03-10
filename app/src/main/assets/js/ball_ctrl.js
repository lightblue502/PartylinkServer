var myApp = angular.module("myApp",[]);
myApp.controller('BallCtrl', ['$scope', function($scope){
    console.log("===== BallCtrl =====");

    //web call android UI Ready;
    Android.onUiReady();
}]);
// 'ball' for ball
document.onkeydown = function (button) {
    // console.log(button.code);
    if(button.code == "KeyA")
        ball.accelerate = -1*unit;
    else if(button.code == "KeyD")
        ball.accelerate = 1*unit;
    else if(button.code == "Space")
        ball.gravitySpeed = -20*unit;
    else
        ball.accelerate = 0;
}
document.onkeyup = function (button) {
    if(button.code != "Space")
        ball.accelerate = 0;
}

function move(accelerate){
  if(accelerate > 3){
    ball.accelerate = 1*unit;
  }else if(accelerate < 3){
    ball.accelerate = -1*unit;
  }else{
    ball.accelerate = 0;
  }
}
function jump(){
    ball.gravitySpeed = -20*unit;
}