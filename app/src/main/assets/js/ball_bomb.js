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
	    			new explodeObject(this.x, this.y, this.width, this.height, angle, "deny");
    	}
    }
    this.explode = function (){
    	if (this.y-this.height*2 < ball.y) {
	    	if( (angle>0 && this.x-ball.width < ball.x+ball.width*2 )||// slope down 	\
	    		(angle<0 && this.x+ball.width > ball.x-ball.width*2 )){// slope up 	/
	    		new explodeObject(this.x, this.y, this.width, this.height, angle, "stun");
	    		return;
	    	}
    	}

    	new explodeObject(this.x, this.y, this.width, this.height, angle);
    }

}

function explodeObject (x, y, width, height, angle, type) {
    // bombs.shift();
    currBomb++;
    this.x = x;
    this.y = y;
    this.angle = angle;
    this.type = type;

	this.image = new Image();
	if(type == "deny"){
		play(audio_poof);
		this.image.src = path.explodeDeny;
	    this.width = width;
		this.imageSmoke = new Image();
		this.imageSmoke.src = path.explodeDenySmoke;
		this.smokeWidth = width;
		this.smokeHeight = this.smokeWidth*this.imageSmoke.naturalHeight/this.imageSmoke.naturalWidth;
    	this.time = 50;
	}
	else{
		this.image.src = path.explode;
	    this.width = width*2;
    	this.time = 50;
	}
	this.originHeight = height;
    this.height = this.width*this.image.naturalHeight/this.image.naturalWidth;

	if(type == "stun"){
		ball.stun(this.time);
	}
	explodes.push(this);

	this.update = function() {
		if(this.time <= 0)
			return;

		if(angle>0)
			writeLine("yellow", {x:this.x-ball.width});
		else
			writeLine("yellow", {x:this.x+ball.width});
		writeLine("yellow", {y:this.y-this.originHeight*2});

		if (this.type == "deny") {
			myGameArea.context.globalAlpha = Math.min(this.time,10)/10;
			drawObject(this.image, this.x, this.y-this.height, this.width, this.height, this.angle);
			myGameArea.context.globalAlpha = 1-Math.abs(this.time-25)/25;
			drawObject(this.imageSmoke, this.x, this.y-this.height, this.smokeWidth, this.smokeHeight, this.angle);
			myGameArea.context.globalAlpha = 1;
		}
		else{
			drawObject(this.image, this.x, this.y-this.originHeight, this.width, this.height, this.angle);
		}

		this.time--;
	}
}

function bombActivate () {
	bombs[currBomb].explode();
}

function updateBombs () {

    if (bombs[0]!=null && bombs[0].y+bombs[0].height*2 < 0) {
        bombs.shift();
        currBomb--;
    }
    if (explodes[0]!=null && explodes[0].y+explodes[0].height*2 < 0) {
        explodes.shift();
    }

    for (var i = currBomb; i < bombs.length; i++) {
        bombs[i].update();
    };
    for (var i = 0; i < explodes.length; i++) {
        explodes[i].update();
    };
}
