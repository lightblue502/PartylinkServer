function bombObject (x, y, angle) {
	this.y = y;
	this.x = x;
	this.angle = angle;

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
    // this.initLine = function(){
        this.lu = new point(-this.width, -this.height, this.angle);
        this.ru = new point(-this.width,  this.height, this.angle);
        this.ld = new point( this.width, -this.height, this.angle);
        this.rd = new point( this.width,  this.height, this.angle);
    // }
    this.genPosition = function(){
        this.plu = pointPosition(this, this.lu);
        this.pru = pointPosition(this, this.ru);
        this.pld = pointPosition(this, this.ld);
        this.prd = pointPosition(this, this.rd);
    }
    this.checkBomb = function () {
    	if (this.y-this.height*2 < focus) {
	    	if( (angle>0 && this.x < ball.x-ball.width )||// slope down \
	    		(angle<0 && this.x > ball.x+ball.width ) )// slope up 	/
	    			new explodeObject(this.x, this.y, this.width, this.height, angle);
    	}
    }
    this.explode = function (){
    	if (this.y-this.height*2 < ball.y) {
	    	if( (angle>0 && this.x-ball.width < ball.x+ball.width*2 )||// slope down 	\
	    		(angle<0 && this.x+ball.width > ball.x-ball.width*2 )){// slope up 	/
	    		new explodeObject(this.x, this.y, this.width, this.height, angle, true);
	    		return;
	    	}
    	}

    	new explodeObject(this.x, this.y, this.width, this.height, angle);
    }

}

function explodeObject (x, y, width, height, angle, stun) {
    bombs.shift();
    this.time = 50;
    this.x = x;
    this.y = y;
    this.angle = angle;

	this.image = new Image();
	this.image.src = path.explode;
    this.width = width*2;
    this.originHeight = height;
    this.height = this.width*this.image.naturalHeight/this.image.naturalWidth;

	if(stun == true){
		ball.stun(this.time);
	}
	explodes.push(this);

	this.update = function() {
		if(angle>0)
			writeLine("yellow", {x:this.x-ball.width});
		else
			writeLine("yellow", {x:this.x+ball.width});
		writeLine("yellow", {y:this.y-this.originHeight*2});

		drawObject(this.image, this.x, this.y-this.height, this.width, this.height, this.angle);
		this.time--;
		if(this.time <= 0)
			explodes.shift();
	}
}

function bombActivate () {
	bombs[0].explode();
}