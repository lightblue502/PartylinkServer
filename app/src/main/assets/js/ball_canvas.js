function startGame() {
    var width = window.innerWidth;
    var height = window.innerHeight;
    lines = [];
    bombs = [];
    edge = {};
    focus = 0;
    unit = 0.001*height;
    startPos = 0;
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

function updateLines () {
    var canvas = myGameArea.canvas;
    var gap = lines[0].height*2 + levelSpace*canvas.height;

    if (Math.max(lines[0].pld.y, lines[0].prd.y) < 0) {
        lines[1].y = lines[0].y + gap;
        lines.shift();
    }
    var numToCreate = Math.ceil(canvas.height/gap) - lines.length + 3;

    for (var i = 0; i < numToCreate; i++) { // create new line
        var x = (lines.length)%2;
        var line = new component(lines[0].width, lines[0].height, lines[0].image,
         (x != 0)? canvas.width-lines[0].x : lines[0].x, 
         lines[0].y + gap*lines.length, lines[0].type);

        line.initLine(lines[lines.length-1].id + 1);
        lines.push(line);
    }

    for (var i = 0; i < lines.length; i++) {
        if(i > 0)
            lines[i].y = lines[i-1].y + gap;
        lines[i].update();
    }
}
function updateBombs () {

    if (bombs[0]!=null && bombs[0].y+bombs[0].height*2 < 0) {
        bombs.shift();
    }

    for (var i = 0; i < bombs.length; i++) {
        bombs[i].update();
    };
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
function updateDistance (gap) {
    if(gap > 0)
        if(ball.distance<0)
            if(Math.min(lines[0].plu.y, lines[0].pru.y) < ball.maxLevel+ball.width){
                ball.distance = 0; //start counting
            }

    var width = myGameArea.canvas.width,
        height = myGameArea.canvas.height,
        padX = 5,
        padY = 3,
        fontSize = 5,
        text;
    text = ((ball.distance<0)?0:ball.distance);
    text = Math.floor(text);
    text = text + " m";
    __writeDistance(text, width - width*padX/100, height*padY/100, fontSize);

    function __writeDistance (text, x, y, h) {
        var ctx = myGameArea.context;
        ctx.font = fontSize+"vh cursive";
        ctx.textAlign="right"; 
        ctx.textBaseline="top"; 

        var gradient=ctx.createLinearGradient(0,y,0, y+h*height/100);
        gradient.addColorStop("0","blue");
        gradient.addColorStop("1","lightblue");

        ctx.lineWidth=2;
        ctx.strokeStyle="gray";
        ctx.strokeText(text, x, y);
        ctx.fillStyle=gradient;
        ctx.fillText(text, x, y);
    }
}


/* ================================= OFFICIAL ===============================*/

    function drawObject(image, x, y, width, height, angle) { 
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

    var write = [];
    function writeLine (color, p1, p2) {
        if(debugEnable){

            if(p2 == null)
                p2 = {};
            if(p1.x == null){
                p1.x = 0;
                p2.x = myGameArea.canvas.width;
                p2.y = p1.y;
            }
            if(p1.y == null){
                p1.y = 0;
                p2.y = myGameArea.canvas.height;
                p2.x = p1.x;
            }

            write.push({color:color, p1:p1, p2:p2});
        }
    }

    function showDedugLine(){
        if(debugEnable){
            var context = myGameArea.context;

            for (var i = 0; i < write.length; i++) {
                context.beginPath();
                context.moveTo(write[i].p1.x, write[i].p1.y);
                context.lineTo(write[i].p2.x, write[i].p2.y);
                context.strokeStyle=write[i].color;
                context.lineWidth=2;
                context.stroke();
                context.closePath();
            };

            write = [];
        }
    }