
// $(function () {
//   $('#myInput').addClass('open');
//   $('#myInput, #myInput button.close').on('keyup', function(event) {
//     if (event.keyCode == 27 || event.keyCode == 8) {
//       again();
//     }
//   });

//   $('#myInput, #myInput button.close').on('click', function(event) {
//     again()
//   });
//   function again(){
//     $(this).removeClass('open');
//     window.location.replace("/plusNumber/");
//   }

// });


var app = angular.module("myApp", []);
app.controller('NumericCtrl', ['$scope','$interval', function($scope, $interval){
  console.log("===== NumericCtrl =====");

  var a1 = new Person(1,"John",0,"img/637.jpg");
  // var a2 = new Person(3,"Jane",0);
  var b1 = new Person(2,"Sam",1,"img/coby.jpg");
  // var b2 = new Person(4,"",1);

  $scope.allPlayers=[a1,b1];
  $scope.allPlayers=[];
  $scope.teamA= $scope.allPlayers.filter(function(p){return p.team==0;});
  $scope.teamB= $scope.allPlayers.filter(function(p){return p.team==1;});
  $scope.display = "display center"

  var time;
  $scope.score = {'A':0, 'B':0};
  $scope.round = 1;
  $scope.displayText = "Ready !!";

  //generate ICON WinRoundA nad WinRoundB = 0
  $scope.iconScore = generateIconScore(3, 0 ,0);

  var playing = function(){
    $scope.viewer = "open";
    $scope.showStatus = "playing";
    // $scope.countdown = 3;
    // time = $interval( function(){ 
    //   if($scope.countdown <= 0){
    //     $scope.displayTextCSS = "displayText center bigFont"
    //     // $scope.displayText = ans;

    //     if($scope.countdown <= -5){
    //       // $scope.viewer = "close";
    //       $interval.cancel(time);
    //     }

    //   }
      
    //     $scope.countdown--;
    // }, 1000);
  };
  var ready =function(){
    $scope.showStatus = "Ready";
  }
  playing();

  //web call android UI Ready;
  Android.onUiReady();

}]);
  
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

function getQuestion(params){
  console.log("hello getQuestion");
  angular.element(document.body).scope().$apply(function($scope){
    if(params == "ready"){
      $scope.showStatus = "ready";
      $scope.displayText = "Ready !!";
    }
    else {
      $scope.showStatus = "NEW QUESTION";
      var number1  =  params[0];
      var number2 = params[1];
      var symbol = params[2];
      var ans = params[3];
      $scope.displayText = number1 + " "+ symbol + " " + number2 + " = ?";
    }
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
