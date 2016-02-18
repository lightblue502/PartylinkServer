function getResultScores(resultScores){
  console.log(resultScores);
  angular.element(document.body).scope().$apply(function($scope){
    $scope.resultScores = resultScores;

    var recentGame = resultScores[resultScores.length-1];
    if(recentGame.winRoundA == recentGame.winRoundB)
      recentGame.team = "DRAW";
    else
      recentGame.team = (recentGame.team == "teamA") ? "RED Win": "BLUE Win";
    $scope.recentGame = recentGame; 
  });
}

function playerConfirm(clientId){
	var person = getPerson(clientId);
  	angular.element(document.body).scope().$apply(function($scope){
    console.log(person);
    $scope.allPlayers.push(person);
  	// if(person.team == 'teamA')
  	// 	$scope.showTeamA.push(person)
  	// else
  	// 	$scope.showTeamB.push(person);
  });
}

function getPerson(personId){
  var aa;
  angular.element(document.body).scope().$apply(
    function($scope){
      aa = $scope.teamA.filter(function(p){return p.id==personId;})[0];
      if(aa == null)
        aa = $scope.teamB.filter(function(p){return p.id==personId;})[0];
    });
  if(aa == null)
    console.log("ERROR : getPerson id:"+personId);
  return aa;
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

function getCountdown(countdown){
  angular.element(document.body).scope().$apply(function($scope){
    $scope.countdown = countdown;
  });
}