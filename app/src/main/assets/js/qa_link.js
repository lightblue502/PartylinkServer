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
    var maxRound = 3;
    var iconClass =  "glyphicon glyphicon-tower "
    $scope.iconRed = [];
    $scope.iconBlue = [];

    for (var i = 1; i <= maxRound;) { 
      if(i <= winRoundA){
        $scope.iconBlue.push(iconClass+"iconBlueLight");
      }else{
        $scope.iconBlue.push(iconClass+"iconBlueDark");
      }
      i++;
    }

    for (var j = 1; j <= maxRound;) { 
     if(j <= winRoundB){
        $scope.iconRed.push(iconClass+"iconRedLight");
      }else{
        $scope.iconRed.push(iconClass+"iconRedDark");
      }
      j++;
    }
    // console.log($scope.iconRed);
    // console.log($scope.iconBlue);
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

function ask(id, team){
  resetStage();
  console.log("id:"+id+" "+team+" shake");
  var elem = document.getElementById(id);
  var person = getPerson(id, team);
  elem.style.visibility = "hidden";

  angular.element(document.body).scope().$apply(
    function($scope){
        $scope.show.push(person);
      console.log(person);
    });
  return true;
}

function resetStage () {
  var clone, elem;
  angular.element(document.body).scope().$apply(
  function($scope){
    clone = $scope.show;
    $scope.show = [];
  });

  clone.forEach(function(person){
    elem = document.getElementById(person.id);
    elem.style.visibility = "visible";
  });
}