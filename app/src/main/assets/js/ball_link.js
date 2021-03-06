function start () {
    startGame();

    angular.element(document.body).scope().$apply(function($scope){
        $scope.show = "";
        $scope.dimDisplay = "animated zoomOut ";
    });
    
}

function stop(){
    myGameArea.pause();
    Android.sendScore(ball.distance);
	console.log("Game Stop");
    angular.element(document.body).scope().$apply(function($scope){
        $scope.show = "";
        $scope.showClass = "";
        setTimeout(function() {
            $scope.$apply(function () {
                $scope.show = GAMEteam+" score: "+Math.floor(ball.distance)+" m";
                $scope.dimDisplay = "animated fadeIn ";
                $scope.showClass = "animated zoomIn "+GAMEteam;
            });
        },100);
    });
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
    Android.sendScore(ball.distance);
    console.log("Game pause");
    angular.element(document.body).scope().$apply(function($scope){
        $scope.show = "pause";
        $scope.showClass = "";
        setTimeout(function() {
            $scope.$apply(function () {
                $scope.dimDisplay = "animated fadeIn ";
                $scope.showClass = "animated infinite pulse ";
            });
        },100);
    });
}

function resume(){
    myGameArea.resume();

    angular.element(document.body).scope().$apply(function($scope){
        $scope.show = "";
        $scope.dimDisplay = "animated zoomOut ";
    });
}

function getInitialBomb(initial_bomb){
  bombPos = initial_bomb;
};

function getCurrentScore(scoreB ,scoreA){
	console.log("getCurrentScore");
	console.log(scoreA);
	console.log(scoreB);
    angular.element(document.body).scope().$apply(function($scope){
        $scope.score.A = scoreA;
        $scope.score.B = scoreB;
    });
};

function getRound(round){
	console.log("getRound");
	console.log(round);
    GAMEround = round;
}
function playerTeam (team) {
    if(team == "teamA")
        GAMEteam = "red";
    else if(team == "teamB")
        GAMEteam = "blue";
    else
        GAMEteam = "white";
}

function getWinRound(winRoundB, winRoundA){
	console.log("getWinRound");
	// console.log(winRoundA);
	// console.log(winRoundB);
    angular.element(document.body).scope().$apply(function($scope){
        console.log("winRoundA :"+winRoundA );
        console.log("winRoundB :"+winRoundB );

        $scope.iconScore = generateIconScore(3, winRoundA, winRoundB);
        
    });
}


function initPlayer(teams){
	console.log("initPlayer");
    console.log(teams);
    angular.element(document.body).scope().$apply(function($scope){
        teams.forEach(function(team, index){
            team.forEach(function(player){
                if(index == 0){
                    $scope.teamA.push(new Person(player.id, player.name,'teamA', player.icon) );
                }
                else{
                    $scope.teamB.push(new Person(player.id, player.name,'teamB', player.icon) );
                }
            });
        });
    });
}

function getCountdown(countdown){
	console.log("countdown");
	console.log(countdown);
    if(countdown){
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
}

var current_audio;
function play(src){
  if(current_audio != null)
    current_audio.remove();
  current_audio = new Audio(src);
  if(src == audio_explode)
    current_audio.currentTime = 1;
  else if(src == audio_jump)
    current_audio.currentTime = 0;
  else if(src == audio_poof)
    current_audio.currentTime = 0.6;
  current_audio.play();
}

// getCurrentScore
// getWinRound
// getRound
// initPlayer
// getCountdown