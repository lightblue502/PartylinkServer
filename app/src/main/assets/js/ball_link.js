function start () {
    startGame();
}

function move(accelerate){
    if(accelerate > 3){
        ball.accelerate = 1*unit;
    }else if(accelerate < 3){
        ball.accelerate = -1*unit;
    }else{
        ball.accelerate = 0;
    }
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