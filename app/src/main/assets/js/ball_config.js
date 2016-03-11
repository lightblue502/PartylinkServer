var TO_RADIANS = Math.PI/180; 
var ball;
var lines;
var bombs;
var edge;
var lineWidth = 0.85; // width in % of x
var lineAngle = 5;
var levelSpace = 0.5; // space from line a to line b in y-axis
var levelToShift = 0.4; // pos y-axis % to shift background
var focus; //focus point in y-axis
var unit;
var debugEnable = true;
var startPos;
var bombPos = [[0,1]];

var path = {
	ball: "img/ball/ball.png",
	// bomb: "img/ball/bomb.png",
	bomb: "img/ball/bomb_lineup.png",
	line: "img/ball/ground.png",
	lineEdgeL: "img/ball/ground-edge-left.png",
	lineEdgeR: "img/ball/ground-edge-right.png"
};