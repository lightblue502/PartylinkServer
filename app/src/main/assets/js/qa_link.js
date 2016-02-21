var audio_correct = "sounds/correct.mp3";
var audio_wrong = "sounds/wrong.mp3";
var audio_ding = "sounds/ding.wav";

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
  elem.className += " animated zoomOut showAnimated";

  angular.element(document.body).scope().$apply(
    function($scope){
      $scope.status = "ask "+$scope.animate;
      $scope.show.push(person);
      console.log(person);
      $scope.showAnimate();
    });
  play(audio_ding);
  return true;
}

function correct(id, string){
  var elem = document.getElementById(id);
  var person = getPerson(id);

  angular.element(document.body).scope().$apply(
    function($scope){
      clearLayer($scope);
      $scope.status = "correct "+$scope.animate;
      $scope.show.push(person);
      console.log(person);

      console.log("id:"+id+" correct");
      elem.className += " animated zoomOut showAnimated";
      $scope.showAnimate();
      $scope.answer = string;
      $scope.answerClass = "animated tada"
    });
  play(audio_correct);
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
      elem.className += " animated zoomOut showAnimated";
      $scope.showAnimate();
    });
  play(audio_wrong);
  return true;
}


function resetStage () {
  var clone, elem;
  angular.element(document.body).scope().$apply(
  function($scope){
    clone = $scope.show;
    $scope.show = [];
    $scope.answer = "";
  });

  clone.forEach(function(person){
    elem = document.getElementById(person.id);
    var a = elem.className.split(" ");
    elem.className = a[0]+" "+a[1]+" animated bounceIn";
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
    var a = elem.className.split(" ");
    elem.className = a[0]+" "+a[1]+" animated bounceIn";
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

var current_audio;
function play(src){
  if(current_audio != null)
    current_audio.remove();
  current_audio = new Audio(src);
  current_audio.play();
}