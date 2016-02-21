var audio_correct = new Audio("sounds/correct.mp3");
var audio_wrong = new Audio("sounds/wrong.mp3");
var audio_ding = new Audio("sounds/ding.wav");

function generateIconScore(maxIcon, winRoundA, winRoundB){
  var icons = [
    {
      name :"red", winRound: winRoundA, colors:{dark :"#4E0404", light:"#F55"}
    },
    {
      name :"blue", winRound: winRoundB, colors:{dark :"#0B2B46", light:"#5096FF"}
    }
  ];
  var data = {};
  for(index in icons){
    var icon = icons[index];
    data[icon.name] = [];
    if(icon.winRound > 0){
      for (var j = 0; j < icon.winRound; j++) {
        data[icon.name].push({color:icon.colors.light});
      }
    }
    for (var i = 0; i < maxIcon - icon.winRound; i++) {
      data[icon.name].push({color:icon.colors.dark});
    }
    
  }
  return data;
}

function Person(id, name, team, icon) {
  this.id = id;
  this.name = name || "Anonymous";
  this.icon = icon || "img/person.png";
  this.team = team;
}

function getCurrentScore(scoreA ,scoreB){
  console.log("hello getCurrentScore");
  angular.element(document.body).scope().$apply(function($scope){
          $scope.score = {'A':scoreA, 'B':scoreB};
  });
};

function getSolves(params){
    var solve  =  params[0];
    var clientId = params[1];
    var team = params[2];
    console.log(clientId + " " +team);
    var person = getPerson(clientId);
    angular.element(document.body).scope().$apply(function($scope){
      $scope.display = "display status center"
      // $scope.displayText = "Solve = "+solve+" "+person.name+" "+person.team;
      // $scope.displayText = "correct!! "+person.name+" "+person.team;
      $scope.displayText = person.name+" Correct!! ";
      audio_correct.play();
  });
}

function getQuestion(params){
  console.log("hello getQuestion");
  angular.element(document.body).scope().$apply(function($scope){
    if(params == "ready"){
      $scope.display = "display status center";
      $scope.showStatus = "ready";
      $scope.displayText = "Ready !!";
    }
    else {
      $scope.showStatus = "NEW QUESTION";
      var number1  =  params[0];
      var number2 = params[1];
      var symbol = params[2];
      var ans = params[3];
      $scope.display = "display content center"
      $scope.displayText = number1 + " "+ symbol + " " + number2 + " = ?";
    }
  });
};

function getRound(round){
  angular.element(document.body).scope().$apply(function($scope){
    audio_ding.play();
    $scope.display = "display status center";
    $scope.displayText = "ROUND" + round;
  });
  getCurrentScore(0,0);
};

function getWinRound(winRoundA, winRoundB){
  angular.element(document.body).scope().$apply(function($scope){
    console.log("winRoundA :"+winRoundA );
    console.log("winRoundB :"+winRoundB );

    $scope.iconScore = generateIconScore(3, winRoundA, winRoundB);
    
  });
}

function initPlayer(teams){
  angular.element(document.body).scope().$apply(function($scope){
    teams.forEach(function(team, index){
      team.forEach(function(player){
        if(index == 0){
          $scope.teamA.push(new Person(player.id, player.name, 'A') );
        }
        else{
          $scope.teamB.push(new Person(player.id, player.name, 'B') );
        }
      });
    });
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

function getCountdown(countdown){
  angular.element(document.body).scope().$apply(function($scope){
    
    if(countdown <= 3){
      $scope.display = "display status center";
      $scope.displayText = countdown;
    }

  });
}