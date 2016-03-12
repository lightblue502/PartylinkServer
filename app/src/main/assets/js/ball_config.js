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
var bombPos = [[-1],[0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1],
[0.2,0.7],[0.1,0.5],[0.4,0.8],[0.2,0.3],[0.6]];

var path = {
	ballL: "img/ball/ball-look-left.png",
	ballLHead: "img/ball/ball-head-look-left.png",
	ballR: "img/ball/ball-look-right.png",
	ballRHead: "img/ball/ball-head-look-right.png",
	// bomb: "img/ball/bomb.png",
	bomb: "img/ball/bomb_lineup.png",
	explode: "img/ball/explode.png",
	line: "img/ball/ground.png",
	lineEdgeL: "img/ball/ground-edge-left.png",
	lineEdgeR: "img/ball/ground-edge-right.png",
	bg: "img/ball/BG-rool-2.jpg"
};