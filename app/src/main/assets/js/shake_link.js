var audio_shaking = new Audio("sounds/ElectricityShock.mp3");



function getCurrentScore(scoreA ,scoreB){
  // console.log("hello getCurrentScore");
  angular.element(document.body).scope().$apply(function($scope){
  audio_shaking.play();
    if($scope.realScore.A < scoreA)
        shakeAnimate("left",$scope);
    if($scope.realScore.B < scoreB)
        shakeAnimate("right",$scope);
    $scope.realScore.A = scoreA;
    $scope.realScore.B = scoreB;
  });
};

function getRound(round){
  angular.element(document.body).scope().$apply(function($scope){
    $scope.round = round;
  });
  getCurrentScore(0 ,0);
};

function getWinRound(winRoundA, winRoundB){
  angular.element(document.body).scope().$apply(function($scope){
    console.log("winRoundA :"+winRoundA );
    console.log("winRoundB :"+winRoundB );

    $scope.iconScore = generateIconScore(3, winRoundA, winRoundB);
    
  });
}


function initPlayer(teams){
  console.log(teams);
  angular.element(document.body).scope().$apply(function($scope){
    teams.forEach(function(team, index){
      team.forEach(function(player){
        if(index == 0){
          $scope.teamA.push(new Person(player.id, player.name, 'teamA') );
        }
        else{
          $scope.teamB.push(new Person(player.id, player.name, 'teamB') );
        }
      });
    });
  });
}

function shake(id, team){
  console.log("id:"+id+" "+team+" shake");
  var elem = document.getElementById(id);
  var person = getPerson(id, team);
  elem.style.visibility = "hidden";

  angular.element(document.body).scope().$apply(
    function($scope){
      if(team == 'teamA'){
        $scope.showleft.push(person);
        shakeAnimate("left",$scope);
      }
      else{
        $scope.showright.push(person);
        shakeAnimate("right",$scope);
      }
      updateStyle ($scope)

      console.log(person);
    });
  return true;
}

function resetStage () {
  var cloneA, cloneB, elem;
  angular.element(document.body).scope().$apply(
  function($scope){
    cloneA = $scope.showleft;
    cloneB = $scope.showright;
    $scope.showleft = [];
    $scope.showright = [];
    updateStyle ($scope)
  });

  cloneA.forEach(function(person){
    elem = document.getElementById(person.id);
    elem.style.visibility = "visible";
  });
  cloneB.forEach(function(person){
    elem = document.getElementById(person.id);
    elem.style.visibility = "visible";
  });
}

function getCountdown(countdown){
  angular.element(document.body).scope().$apply(function($scope){
    $scope.countdown = countdown;
  });
}
/******************** test 
initPlayer([[{'id':1,'name':'jjj'}],[{'id':2,'name':'kkkk'}]]);
shake(1,'A'); shake(2,'B');
resetStage();
********************/