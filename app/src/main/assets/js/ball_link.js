
function start () {
    startGame();
}

function move(delta){
    ball.accelerate = delta/10*unit;
}

function jump(){
    ball.gravitySpeed = -20*unit;
}

function pause(){
    myGameArea.pause();
}

function resume(){
    myGameArea.resume();
}

function getInitialBomb(initial_bomb){
  console.log("in getInitialBomb");
  console.log(initial_bomb);
};

