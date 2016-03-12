function start () {
    startGame();
}

function stop(){
    pause();
    Android.sendScore(ball.distance);
	console.log("Game Stop");
}

function move(delta){
    ball.accelerate = delta/10*unit;
}

function jump(){
    ball.gravitySpeed = -20*unit;
    play(audio_jump);
}

function bomb(){
    bombActivate();
    play(audio_explode);
}

function pause(){
    myGameArea.pause();
}

function resume(){
    myGameArea.resume();
}

function getInitialBomb(initial_bomb){
  bombPos = initial_bomb;
  start();
  pause();
};

function getCurrentScore(scoreA ,scoreB){
	console.log("getCurrentScore");
	console.log(scoreA);
	console.log(scoreB);
  // angular.element(document.body).scope().$apply(function($scope){
  // audio_shaking.play();
  //   if($scope.realScore.A < scoreA)
  //       shakeAnimate("left",$scope);
  //   if($scope.realScore.B < scoreB)
  //       shakeAnimate("right",$scope);
  //   $scope.realScore.A = scoreA;
  //   $scope.realScore.B = scoreB;
  // });
};

function getRound(round){
	console.log("getRound");
	console.log(round);
  // angular.element(document.body).scope().$apply(function($scope){
  //   $scope.round = round;
  // });
  // getCurrentScore(0 ,0);
};

function getWinRound(winRoundA, winRoundB){
	console.log("getWinRound");
	console.log(winRoundA);
	console.log(winRoundB);
  // angular.element(document.body).scope().$apply(function($scope){
  //   console.log("winRoundA :"+winRoundA );
  //   console.log("winRoundB :"+winRoundB );

  //   $scope.iconScore = generateIconScore(3, winRoundA, winRoundB);
    
  // });
}


function initPlayer(teams){
	console.log("initPlayer");
  console.log(teams);
  // angular.element(document.body).scope().$apply(function($scope){
  //   teams.forEach(function(team, index){
  //     team.forEach(function(player){
  //       if(index == 0){
  //         $scope.teamA.push(new Person(player.id, player.name,'teamA', player.icon) );
  //       }
  //       else{
  //         $scope.teamB.push(new Person(player.id, player.name,'teamB', player.icon) );
  //       }
  //     });
  //   });
  // });
}

function getCountdown(countdown){
	console.log("countdown");
	console.log(countdown);
  // angular.element(document.body).scope().$apply(function($scope){
  //   $scope.countdown = countdown;
  //   $scope.countClass = "";
  //   setTimeout(function() {
  //     $scope.$apply(function () {
  //       $scope.countClass = "animated fadeOut ";
  //     });
  //   },200);
  // });
}

var current_audio;
function play(src){
  if(current_audio != null)
    current_audio.remove();
  current_audio = new Audio(src);
  current_audio.play();
}

// getCurrentScore
// getWinRound
// getRound
// initPlayer
// getCountdown
