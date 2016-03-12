var TO_RADIANS = Math.PI/180; 
var ball;
var lines;
var bombs;
var explodes;
var edge;
var lineWidth = 0.85; // width in % of x
var lineAngle = 5;
var bombSpace = 1; // available area that can place bomb
var levelSpace = 0.5; // space from line a to line b in y-axis
var levelToShift = 0.5; // pos y-axis % to shift background
var focus; //focus point in y-axis
var unit;
var debugEnable = 0;
var startPos;
var currBomb; 
var bombPos; 
// = [[-1],[0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1],
// [0.2,0.7],[0.1,0.5],[0.4,0.8],[0.2,0.3],[0.6]];

var path = {
	ballL: "img/ball/ball-look-left.png",
	ballLDie: "img/ball/ball-die-left.png",
	ballLHead: "img/ball/ball-head-longlook-left.png",
	ballR: "img/ball/ball-look-right.png",
	ballRDie: "img/ball/ball-die-right.png",
	ballRHead: "img/ball/ball-head-longlook-right.png",
	// bomb: "img/ball/bomb.png",
	bomb: "img/ball/bomb_lineup.png",
	explode: "img/ball/explode.png",
	explodeDeny: "img/ball/explode-deny.png",
	explodeDenySmoke: "img/ball/explode-deny-smoke.png",

	line: "img/ball/ground.png",
	lineEdgeL: "img/ball/ground-edge-left.png",
	lineEdgeR: "img/ball/ground-edge-right.png",
	bg: "img/ball/BG-rool-2.jpg"
};

function preloadImage () {
	for(var i in path){
		__tmp_ = new Image();
		__tmp_.src = path[i];
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