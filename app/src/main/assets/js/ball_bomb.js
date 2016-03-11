function bombObject (x, y, angle, minPos, maxPos, lineId) {
	this.y = y;
	this.x = x;
	this.angle = angle;
	this.min = minPos;
	this.max = maxPos;

	this.color = "yellow";
	this.image = new Image();
	this.image.src = path.bomb;

	this.height = 100*unit;
	// this.width = 100*unit;
    this.width = this.height*this.image.naturalWidth/this.image.naturalHeight;
	bombs.push(this);

    this.update = function() {
    	writeLine("yellow", {y:this.y-ball.height});
        drawObject(this.image, this.x, this.y-this.height, this.width, this.height, this.angle);
        this.checkBomb();
    }
    this.checkBomb = function () {
    	if(ball.maxLevel+ball.height/2 > this.y){
    		bombs.shift();
    	}
    }

}
