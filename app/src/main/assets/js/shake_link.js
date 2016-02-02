function getCurrentScore(scoreA ,scoreB){
  // console.log("hello getCurrentScore");
  angular.element(document.body).scope().$apply(function($scope){
          $scope.score.A = scoreA;
          $scope.score.B = scoreB;
  });
};

function getRound(round){
  angular.element(document.body).scope().$apply(function($scope){
    $scope.round = round;
  });
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
      if(team == 'teamA')
        $scope.showleft.push(person);
      else
        $scope.showright.push(person);
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

/******************** test 
initPlayer([[{'id':1,'name':'jjj'}],[{'id':2,'name':'kkkk'}]]);
shake(1,'A'); shake(2,'B');
resetStage();
********************/