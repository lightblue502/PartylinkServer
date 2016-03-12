function startGame() {
    var width = window.innerWidth;
    var height = window.innerHeight;
    lines = [];
    bombs = [];
    explodes = [];
    edge = {};
    focus = 0;
    unit = 0.001*height;
    startPos = 0;
    currBomb = 0;
    var objSize = 50 * unit;

    ball = new ballObject(objSize, objSize, 80*unit, 80*unit);

    edge.left = new component(objSize/2, height, "edge", 0, height/2);
    edge.right = new component(objSize/2, height, "edge", width, height/2);
    edge.top = new component(width/2, objSize/2, "edge", width/2, -height/2);

    var line = new component(width*lineWidth/2, objSize, "line", width*lineWidth/2, height*3/2);
    line.initLine(0);
    lines.push(line);


    myGameArea.start(width, height);
}

var myGameArea = {
    canvas : document.createElement("canvas"),
    start : function(width, height) {
        this.canvas.width = width;
        this.canvas.height = height;
        this.context = this.canvas.getContext("2d");
        document.body.insertBefore(this.canvas, document.body.childNodes[0]);
        this.resume();
    },
    resume : function() {
        if(this.interval)
            this.pause();
        this.interval = setInterval(updateGameArea, 20); 
    },    
    pause : function() {
        clearInterval(this.interval);
        this.interval = null;
    },    
    clear : function() {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
}

function updateGameArea() {

    myGameArea.clear();
    makeBG();
    updateText();

    ball.newPos();

    if(ball.distance < 0)
        startPos = Math.min(lines[0].plu.y, lines[0].pru.y)-ball.width;

    updateLines();
    updateBombs();
    for(var i in edge){
        edge[i].update();
    }
    var gap = shiftFocus();

    ball.update();
    updateDistance(gap);

    if(debugEnable){ // debug start position
        writeLine("green", {y:startPos});
        var ctx = myGameArea.context;
        ctx.font = "5vh cursive";
        ctx.textAlign="left"; 
        ctx.textBaseline="top"; 
        ctx.fillStyle="black";
        ctx.fillText(Math.round(startPos), 50, 5);
    }
    showDedugLine();
}

function shiftFocus () {
    var gap = focus - levelToShift*myGameArea.canvas.height;
    if(gap > 0){ // shift background
        var shift = gap*0.1 +1*unit;
        ball.y -= shift;
        ball.maxLevel -= shift;
        lines[0].y -= shift;
        focus -= shift;
        startPos -= shift;
        for (var i = 0; i < bombs.length; i++) {
            bombs[i].y -= shift;
        };
        for (var i = 0; i < explodes.length; i++) {
            explodes[i].y -= shift;
        };
    }
    return gap;
}
function makeBG () {
    canvas = myGameArea.canvas;
    bg = new Image();
    bg.src = path.bg;
    BGheight = canvas.height;
    BGwidth = BGheight*bg.naturalWidth/bg.naturalHeight;
    if(BGwidth < canvas.width){
        BGwidth = canvas.width;
        BGheight = BGwidth*bg.naturalHeight/bg.naturalWidth;
    }
    myGameArea.context.drawImage(bg, (BGwidth-canvas.width)/2, canvas.height-BGheight,
     BGwidth, BGheight);
}
function updateText () {
    var ctx = myGameArea.context;
    var fontSize = 20;
    canvas = myGameArea.canvas;
    ctx.font = fontSize+"vh gameOver";
    ctx.textAlign="center"; 
    ctx.textBaseline="top"; 

    ctx.lineWidth=fontSize*unit;
    ctx.strokeStyle="black";
    ctx.fillStyle="white";

    var x = canvas.width/2,
        y = startPos-canvas.height/2 -fontSize/100*canvas.height,
        text = "ROUND "+GAMEround;
    ctx.strokeText(text, x, y);
    ctx.fillText(text, x, y);

    y = startPos-canvas.height/2 +fontSize/100*canvas.height;
    ctx.font = fontSize/2+"vh gameOver";
    ctx.lineWidth=fontSize/1.5*unit;
    ctx.fillStyle=GAMEteam;
    text = GAMEteam.toUpperCase()+" ARE PLAYER";
    ctx.strokeText(text, x, y);
    ctx.fillText(text, x, y);
}
function updateDistance (gap) {
    if(gap > 0)
        if(ball.distance<0)
            if(Math.min(lines[0].plu.y, lines[0].pru.y) < ball.maxLevel+ball.width){
                ball.distance = 0; //start counting
            }

    var width = myGameArea.canvas.width,
        height = myGameArea.canvas.height,
        padX = 4,
        padY = 3,
        fontSize = 5,
        text;
    text = ((ball.distance<0)?0:ball.distance);
    text = Math.floor(text);
    text = text + " m";
    __writeDistance(text, width - width*padX/100, height*padY/100, fontSize);

    function __writeDistance (text, x, y, h) {
        var ctx = myGameArea.context;
        ctx.font = fontSize+"vh gameOver";
        ctx.textAlign="right"; 
        ctx.textBaseline="top"; 

        var gradient=ctx.createLinearGradient(0,y,0, y+h*height/100);
        gradient.addColorStop("0","lightblue");
        gradient.addColorStop("1","black");

        ctx.lineWidth=fontSize*unit;
        ctx.strokeStyle=gradient;
        ctx.strokeText(text, x, y);
        ctx.fillStyle=GAMEteam;
        ctx.fillText(text, x, y);
    }
}

