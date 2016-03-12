var myApp = angular.module("myApp",[]);
myApp.controller('BallCtrl', ['$scope', function($scope){
    console.log("===== BallCtrl =====");

    var a1 = new Person(91,"John",'teamA',"img/637.jpg");
    var a2 = new Person(93,"Jane",'teamA');
    var a3 = new Person(95,"joe",'teamA');
    var b1 = new Person(92,"Sam",'teamB',"img/coby.jpg");
    var b2 = new Person(94,"",'teamB');
    var b3 = new Person(96,"eiei",'teamB');

    // $scope.teamA= [a1,a2,a3];
    // $scope.teamB= [b1,b2,b3];
    $scope.teamA= [];
    $scope.teamB= [];
    $scope.score={"A":0, "B":0};

    $scope.iconScore = generateIconScore(3, 0 ,0); // 3 is max round

    //web call android UI Ready;
    Android.onUiReady();
}]);

// 'ball' for ball
document.onkeydown = function (button) {
    // console.log(button.code);
    if(button.code == "KeyA")
        ball.accelerate = -1*unit;
    else if(button.code == "KeyD")
        ball.accelerate = 1*unit;
    else if(button.code == "KeyS"){
        if(myGameArea.interval)
            myGameArea.pause();
        else
            myGameArea.resume();
    }
    else if(button.code == "KeyN")
        start();
    else if(button.code == "KeyE")
        bomb();
    else if(button.code == "Space")
        ball.gravitySpeed = -20*unit;
    else
        ball.accelerate = 0;
}
document.onkeyup = function (button) {
    if(button.code == "KeyA" || button.code == "KeyD")
        ball.accelerate = 0;
}



function Person(id, name, team, icon) {
    this.id = id;
    this.name = name || "Anonymous";
    this.icon = icon || "img/person.png";
    this.team = team;
}

function getPerson(personId, team){
    var aa;
    angular.element(document.body).scope().$apply(
        function($scope){
            if(team == 'teamA')
                aa = $scope.teamA.filter(function(p){return p.id==personId;})[0];
            else
                aa = $scope.teamB.filter(function(p){return p.id==personId;})[0];
        });
    if(aa == null)
        console.log("ERROR : getPerson id:"+personId+" team"+team);
    return aa;
}


function generateIconScore(maxIcon, winRoundA, winRoundB){
    var icons = [
        {
            name :"red", winRound: winRoundA, color:{dark :'rgba(255,127,127,0.3)', light:"#F00"}
        },
        {
            name :"blue", winRound: winRoundB, color:{dark :"rgba(127,127,255,0.3)", light:"#00F"}
        }
    ];
    var data = {};
    for(index in icons){
        var icon = icons[index];
        data[icon.name] = {dark:[],light:[]};
        if(icon.winRound > 0){
            for (var j = 0; j < icon.winRound; j++) {
                data[icon.name].light.push({color:icon.color.light});
            }
        }
        for (var i = 0; i < maxIcon - icon.winRound; i++) {
            data[icon.name].dark.push({color:icon.color.dark});
        }
    }
    return data;
}