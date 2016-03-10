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

      if($scope.allPlayers.length == $scope.teamA.length + $scope.teamB.length)
        $scope.conShow = false;
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
          $scope.teamA.push(new Person(player.id, player.name,'teamA', player.icon ));
        }
        else{
          $scope.teamB.push(new Person(player.id, player.name,'teamB', player.icon ));
        }
      });
    });
  });
}

function getCountdown(countdown){
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