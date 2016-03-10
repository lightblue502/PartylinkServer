var TO_RADIANS = Math.PI/180; 
var ball;
var lines = [];
var edge = {};
var lineWidth = 0.85; // width in % of x
var levelSpace = 0.5; // space from line a to line b in y-axis
var levelToShift = 0.4; // pos y-axis % to shift background
var focus = 0; //focus point in y-axis
var unit = 0.001;
var debugEnable = false;

function startGame() {
    var width = window.innerWidth;
    var height = window.innerHeight;
    unit *= height;
    var objSize = 0.05 * height;
    ball = new component(objSize, objSize, "img/ball/ball.png", 80*unit, 20*unit, "ball");
    edge.left = new component(objSize, height, "gray", 0, height/2);
    edge.right = new component(objSize, height, "gray", width, height/2);
    edge.top = new component(width/2, objSize, "gray", width/2, -height/2);
    lines.push(new component(width*lineWidth/2, objSize, "blue", width*lineWidth/2, height));
    lines[0].initLine(0);
    lines[0].maxLevel = 0;
    myGameArea.start(width, height);
}

var myGameArea = {
    canvas : document.createElement("canvas"),
    start : function(width, height) {
        this.canvas.width = width;
        this.canvas.height = height;
        this.context = this.canvas.getContext("2d");
        document.body.insertBefore(this.canvas, document.body.childNodes[0]);
        this.interval = setInterval(updateGameArea, 20);        
    },
    stop : function() {
        clearInterval(this.interval);
    },    
    clear : function() {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
}

function updateGameArea() {

    myGameArea.clear();

    ball.newPos();

    var gap = focus - levelToShift*myGameArea.canvas.height;
    if(gap > 0){ // shift background
        ball.y -= gap*0.1 +1*unit;
        ball.maxLevel -= gap*0.1 +1*unit;
        lines[0].y -= gap*0.1 +1*unit;
        focus -= gap*0.1 +1*unit;
    }

    __updateLines();

    for(var i in edge){
        edge[i].update();
    }
    
    ball.update();
    __updateDistance(gap);

    function __updateLines () {
        var canvas = myGameArea.canvas;
        var gap = lines[0].height*2 + levelSpace*canvas.height;

        if (Math.max(lines[0].pld.y, lines[0].prd.y) < 0) {
            lines[1].y = lines[0].y + gap;
            lines.shift();
        }
        var firstSide = (lines[0].x < canvas.width/2)? 0 : 1; // 0:left 1:right
        var numToCreate = Math.ceil(canvas.height/gap) - lines.length;

        for (var i = 0; i < numToCreate+1; i++) { // create new line
            var x = (lines.length)%2;
            lines.push(new component(lines[0].width, lines[0].height, lines[0].color,
             (x != 0)? canvas.width-lines[0].x : lines[0].x, lines[0].y + gap*lines.length));
            lines[lines.length-1].initLine((firstSide+x)%2);
        }

        for (var i = 0; i < lines.length; i++) {
            if(i > 0)
                lines[i].y = lines[i-1].y + gap;
            lines[i].update();
        }
    }

    function __updateDistance (gap) {
        if(gap > 0)
            if(ball.distance<0)
                if(Math.min(lines[0].plu.y, lines[0].pru.y) < ball.maxLevel+ball.width)
                    ball.distance = 0; //start counting

        var width = myGameArea.canvas.width,
            height = myGameArea.canvas.height,
            padX = 5,
            padY = 3,
            fontSize = 5,
            text;
        text = ((ball.distance<0)?0:ball.distance);
        text = Math.floor(text);
        text = text + " m";

        var ctx = myGameArea.context;
        ctx.font = fontSize+"vh cursive";
        ctx.textAlign="right"; 
        ctx.textBaseline="top"; 

        var gradient=ctx.createLinearGradient(0,height*padY/100,0, height*(padY+fontSize)/100);
        gradient.addColorStop("0","blue");
        gradient.addColorStop("1","lightblue");

        ctx.strokeStyle="#424242FF";
        ctx.lineWidth=2;
        ctx.strokeText(text, width - width*padX/100, 0 + height*padY/100);
        ctx.fillStyle=gradient;
        ctx.fillText(text, width - width*padX/100, 0 + height*padY/100);
    }
}



/* ================================= OFFICIAL ===============================*/

function drawRotatedImage(image, x, y, width, height, angle) { 
    context = myGameArea.context;
    context.save(); 
 
    // move to the middle of where we want to draw our image
    context.translate(x, y);
 
    // rotate around that point, converting our 
    // angle from degrees to radians 
    context.rotate(angle * TO_RADIANS);
 
    // draw it up and to the left by half the width
    // and height of the image 
    if(typeof image == "object")
        context.drawImage(image, -width, -height, width*2, height*2);
    else{
        context.fillStyle = image;
        context.fillRect(-width, -height, width*2, height*2);
    }
 
    // and restore the co-ords to how they were when we began
    context.restore(); 
}

function point (x, y, angle) {

    var distR = Math.sqrt(x*x + y*y) * y/Math.abs(y) * x/Math.abs(x);
    // var realAngle = Math.asin(y/distR);
    var realAngle = Math.atan2(y,x);
    this.y = Math.sin(realAngle + (angle * TO_RADIANS))*distR;
    this.x = Math.cos(realAngle + (angle * TO_RADIANS))*distR;
    // this.x = Math.sqrt(distR*distR - y*y) * y/Math.abs(y) * distR/Math.abs(distR);
}
function pointPosition (obj, pos) {
    return { x: obj.x+pos.x, y:obj.y+pos.y};
}

function sqr(x) { return x * x }
function dist2(v, w) { return sqr(v.x - w.x) + sqr(v.y - w.y) }
function refPoint(p, v, w) {
    var l2 = dist2(v, w);
    if (l2 == 0) return v;
    var t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
    t = Math.max(0, Math.min(1, t));// 0-1 check outside line
    return { x: v.x + t * (w.x - v.x), y: v.y + t * (w.y - v.y) };
}
function distToSegment(p, v, w) {
    var dist = dist2(p, refPoint(p, v, w));
    return Math.sqrt(dist); 
}

function writeLine (p1, p2) {
    if(debugEnable){
        var context = myGameArea.context;
        context.beginPath();
        context.moveTo(p1.x, p1.y);
        context.lineTo(p2.x, p2.y);
        context.strokeStyle="red";
        context.lineWidth=2;
        context.stroke();
        context.closePath();
    }
}