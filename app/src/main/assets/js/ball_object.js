
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
    this.distance = -1;

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
            drawRotatedImage(this.color, this.x, this.y, this.width, this.height, this.angle);
        }
    }
    this.newPos = function() {
        this.gravitySpeed += this.gravity * this.mass*unit;
        this.gravitySpeed = Math.min(this.gravitySpeed,11*unit);
        this.speedX += this.accelerate;
        this.speedX -= this.speedX * this.gravity;
        this.x += this.speedX + this.accelerate;
        this.y += this.speedY + this.gravitySpeed;
        this.angle += this.speedAngle/unit;
        this.speedAngle *= 0.99;

        if(this.maxLevel < this.y){
            if(this.distance >=0){
                this.distance += (this.y - this.maxLevel)*100/(levelSpace*myGameArea.canvas.height);
            }
            this.maxLevel = this.y;//Math.max(this.y, (lines[2]?lines[1].y:0));/*(lines[0].plu?lines[0].plu.y:0)*/
            focus = Math.max(this.y, focus);
        }
        writeLine({x:0, y:this.maxLevel}, {x:myGameArea.canvas.width, y:this.maxLevel});

        for(var i in edge){
        	this.chechBounceWith(edge[i]);
        }

        for (i = 0; i < lines.length; i += 1) {
        	if(this.checkLine(lines[i]))
                focus = Math.max(lines[i].plu.y, lines[i].pru.y, focus);
        }
        writeLine({x:0, y:focus}, {x:myGameArea.canvas.width, y:focus});
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
    this.initLine = function(side){ // for line

    	if(this.lu == null){
    		var angle = 5;
    		if( side == 0){
    			this.angle = angle;
    		}
    		else{
    			this.angle = -angle;
    		}
    		this.lu = new point(-this.width, -this.height, this.angle);
    		this.ru = new point(-this.width,  this.height, this.angle);
    		this.ld = new point( this.width, -this.height, this.angle);
    		this.rd = new point( this.width,  this.height, this.angle);
    	}
    }
    this.genPosition = function(){ // for line
		this.plu = pointPosition(this, this.lu);
		this.pru = pointPosition(this, this.ru);
		this.pld = pointPosition(this, this.ld);
		this.prd = pointPosition(this, this.rd);
    }
    this.checkCollision = function(p1, p2) { 
    	var dist = distToSegment(this, p1, p2); //distant point to line
    	writeLine(p1, p2);

    	if( dist - this.width < 0 ){
    		this.layOnCollision(p1, p2, dist, this.width-dist);
    	// console.log(dist - this.width);
    		return true;
    	}
    	// console.log(p1, p2);
    	return false; 
    }
    this.layOnCollision = function(p1, p2, originDist, padding) { 
    	var ref = refPoint(this, p1, p2);

    	var forceX = padding/originDist*(this.x - ref.x);
    	var forceY = this.gravitySpeed + padding/originDist*(this.y - ref.y);

    	this.speedX += forceX * this.gravity;
    	this.gravitySpeed -= forceY * this.gravity;
        this.speedAngle = this.speedX*2.5;

    	this.x += padding/originDist*(this.x - ref.x);
    	this.y += padding/originDist*(this.y - ref.y);
    }
    this.checkLine = function(line) { 
    		line.genPosition();
    	//if on areana
    	writeLine(line.pru, line.plu);
    	if(	this.y + this.width > Math.min(line.lu.y, line.ru.y)+line.y && 
    		this.y - this.width < Math.max(line.ld.y, line.rd.y)+line.y){
    		line.genPosition();
    		var u = this.checkCollision(line.plu, line.pru);
    		var d = this.checkCollision(line.pld, line.prd);
    		var l = this.checkCollision(line.plu, line.pld);
    		var r = this.checkCollision(line.pru, line.prd);

            if(u||d||l||r) return true;
            else return false;
    	}
        return false;

    }
}