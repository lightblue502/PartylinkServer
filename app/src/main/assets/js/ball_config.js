var TO_RADIANS = Math.PI/180; 
var ball;
var lines;
var bombs;
var edge;
var lineWidth = 0.85; // width in % of x
var lineAngle = 5;
var bombSpace = 1; // available area that can place bomb
var levelSpace = 0.5; // space from line a to line b in y-axis
var levelToShift = 0.4; // pos y-axis % to shift background
var focus; //focus point in y-axis
var unit;
var debugEnable = false;
var startPos;
var bombPos = [[0],[0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1],
[0.2,0.7],[0.1,0.5],[0.4,0.8],[0.2,0.3],[0.6],[]];

var path = {
	ball: "img/ball/ball.png",
	ballHead: "img/ball/ball-head.png",
	// bomb: "img/ball/bomb.png",
	bomb: "img/ball/bomb_lineup.png",
	line: "img/ball/ground.png",
	lineEdgeL: "img/ball/ground-edge-left.png",
	lineEdgeR: "img/ball/ground-edge-right.png",
	bg: "img/ball/BG-rool-2.jpg"
};