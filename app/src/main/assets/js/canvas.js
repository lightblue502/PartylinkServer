var TO_RADIANS = Math.PI/180; 
var ball;
var lines = [];
var edge = {};
var lineWidth = 0.85;
var levelSpace = 0.3;
var levelToShift = 0.4;
var unit = 0.001;

function startGame() {
    var width = window.innerWidth;
    var height = window.innerHeight;
    unit *= height;
    var objSize = 0.05 * height;
    ball = new component(objSize, objSize, "img/ball/ball.png", 80*unit, 20*unit, "ball");
    edge.left = new component(objSize, height/2, "gray", 0, height/2);
    edge.right = new component(objSize, height/2, "gray", width, height/2);
    lines.push(new component(width*lineWidth/2, objSize, "blue", width*lineWidth/2, height));
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

function component(width, height, color, x, y, type) {
    this.type = type;
    if (type == "ball") {
        this.image = new Image();
        this.image.src = color;
    }
    this.color = color;
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;    
    this.speedX = 0;
    this.speedY = 0;
    this.angle = 0;
    this.speedAngle = 0;
    this.accelerate = 0;
    this.gravity = 0.1;
    this.mass = 10;
    this.gravitySpeed = 0;
    this.bounce = 0.6;
    this.maxLevel = 0; //lowest height that ball go

    this.update = function() {
        ctx = myGameArea.context;
        if (type == "ball") {
            drawRotatedImage(this.image, this.x, this.y, this.width, this.height, this.angle);
        } 
        else if (type == "circle"){
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.width, 0, 2 * Math.PI, true);
            ctx.closePath();
            ctx.fillStyle = this.color;
            ctx.fill();
        }
        else {
            ctx.fillStyle = this.color;
            ctx.fillRect(this.x-this.width, this.y-this.height, this.width*2, this.height*2);
        }
    }
    this.newPos = function() {
        this.gravitySpeed += this.gravity * this.mass*unit;
        this.gravitySpeed = Math.min(this.gravitySpeed,11*unit);
        this.speedX += this.accelerate;
        this.speedX -= this.speedX * this.gravity;
        this.x += this.speedX + this.accelerate;
        this.y += this.speedY + this.gravitySpeed;
        this.maxLevel = Math.max(this.maxLevel, this.y);
        this.angle += this.speedAngle;
        this.speedAngle *= 0.99;

        this.chechBounceWith(edge.left);
        this.chechBounceWith(edge.right);
        for (i = 0; i < lines.length; i += 1) {
            this.chechBounceWith(lines[i]); 
        }
    }
    this.chechBounceWith = function(rectobj){
        var distX = this.x - rectobj.x;// rect are origin
        var distY = this.y - rectobj.y;// rect are origin
        var padX = this.width + rectobj.width;
        var padY = this.width + rectobj.height;
        if(Math.abs(distY) - padY < -this.width/2){
            if(distX < 0 && distX + padX > 0){
                this.x = rectobj.x - padX;
                this.speedX = -this.speedX;
                this.speedAngle = -this.gravitySpeed*2;
            }
            else if(distX > 0 && distX - padX < 0){
                this.x = rectobj.x + padX;
                this.speedX = -this.speedX;
                this.speedAngle = this.gravitySpeed*2;
            }
        }
        if(Math.abs(distX) - padX < -this.width/2){
            if(distY < 0 && distY + padY > 0){
                this.y = rectobj.y - padY;
                this.gravitySpeed = -(this.gravitySpeed * this.bounce);
                this.speedAngle = this.speedX*2;
            }
            else if(distY > 0 && distY - padY < 0){
                this.y = rectobj.y + padY;
                this.gravitySpeed = -(this.gravitySpeed * this.bounce);
                this.speedAngle = -this.speedX*2;
            }
        }
    }
}

function updateGameArea() {

    myGameArea.clear();

    ball.newPos();

    var gap = ball.maxLevel - levelToShift*myGameArea.canvas.height;
    if(gap > 0){
        ball.y -= gap*0.1 +1*unit;
        ball.maxLevel -= gap*0.1 +1*unit;
        lines[0].y -= gap*0.1 +1*unit;
    }

    updateLines();
    edge.left.update();
    edge.right.update();
    ball.update();
}

function updateLines () {
    var canvas = myGameArea.canvas;
    var gap = lines[0].height*2 + levelSpace*canvas.height;

    if (lines[0].y + lines[0].height < 0) {
        lines[1].y = lines[0].y + gap;
        lines.shift();
    }

    var firstSide = (lines[0].x < canvas.width/2)? 0 : 1; // 0:left 1:right
    var numToCreate = Math.ceil(canvas.height/gap) - lines.length;

    for (var i = 0; i < numToCreate; i++) { // create new line
        var x = (lines.length)%2;
        lines.push(new component(lines[0].width, lines[0].height, lines[0].color,
         (x != 0)? canvas.width-lines[0].x : lines[0].x, lines[0].y + gap*lines.length));
    }

    for (var i = 0; i < lines.length; i++) {
        if(i > 0)
            lines[i].y = lines[i-1].y + gap;
        lines[i].update();
    }
}

function drawRotatedImage(image, x, y, width, height, angle) { 
    context = myGameArea.context;
    // save the current co-ordinate system 
    // before we screw with it
    context.save(); 
 
    // move to the middle of where we want to draw our image
    context.translate(x, y);
 
    // rotate around that point, converting our 
    // angle from degrees to radians 
    context.rotate(angle * TO_RADIANS/unit);
 
    // draw it up and to the left by half the width
    // and height of the image 
    context.drawImage(image, -width, -height, width*2, height*2);
 
    // and restore the co-ords to how they were when we began
    context.restore(); 
}
