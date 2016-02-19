function getCurrentScore(scoreA ,scoreB){
  // console.log("hello getCurrentScore");
  angular.element(document.body).scope().$apply(function($scope){
          $scope.score.A = (0 <= scoreA && scoreA < 10)? "0"+scoreA : scoreA;
          $scope.score.B = (0 <= scoreB && scoreB < 10)? "0"+scoreB : scoreB;
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

function ask(id, team){
  resetStage();
  console.log("id:"+id+" "+team+" ask");
  var elem = document.getElementById(id);
  var person = getPerson(id, team);
  elem.style.visibility = "hidden";

  angular.element(document.body).scope().$apply(
    function($scope){
      $scope.status = "ask "+$scope.animate;
      $scope.show.push(person);
      console.log(person);
    });
  return true;
}

function correct(id){
  var elem = document.getElementById(id);
  var person = getPerson(id);

  angular.element(document.body).scope().$apply(
    function($scope){
      clearLayer($scope);
      $scope.status = "correct "+$scope.animate;
      $scope.show.push(person);
      console.log(person);

      console.log("id:"+id+" correct");
      elem.style.visibility = "hidden";
    });
  return true;
}
function wrong(id){
  var elem = document.getElementById(id);
  var person = getPerson(id);

  angular.element(document.body).scope().$apply(
    function($scope){
      clearLayer($scope);
      $scope.status = "wrong "+$scope.animate;
      $scope.show.push(person);
      console.log(person);

      console.log("id:"+id+" wrong");
      elem.style.visibility = "hidden";
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

function clearLayer ($scope) { //allow just ask 
  var clone , elem, tmp;
  // angular.element(document.body).scope().$apply(
  // function($scope){
    tmp = $scope.show.splice(0, 1);
    clone = $scope.show;
    $scope.show = tmp;
    console.log("splice",$scope.show);
  // });

  clone.forEach(function(person){
    elem = document.getElementById(person.id);
    elem.style.visibility = "visible";
  });
}

function getCountdown(countdown){
  angular.element(document.body).scope().$apply(function($scope){
    $scope.countdown = countdown;
  });
}