
function component(width, height, color, x, y, type) {
    this.type = type;
    this.image = color;
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;    
    this.angle = 0;

    this.update = function() {
        if (this.image == "edge") {
            drawLineImage(this.x, this.y, this.width, this.height, this.angle, "#246A86");
        }
        else if(this.image == "line"){
            drawLineImage(this.x, this.y, this.width, this.height, this.angle);
        }
    }
    this.initLine = function(id){ // for line
        this.id = id;
        // console.log(id);
        side = id%2;

        if( side == 0){
            this.angle = lineAngle;
        }
        else{
            this.angle = -lineAngle;
        }
        this.lu = new point(-this.width, -this.height, this.angle);
        this.ru = new point(-this.width,  this.height, this.angle);
        this.ld = new point( this.width, -this.height, this.angle);
        this.rd = new point( this.width,  this.height, this.angle);

        this.genBombs();
    }
    this.genPosition = function(){ // for line
        this.plu = pointPosition(this, this.lu);
        this.pru = pointPosition(this, this.ru);
        this.pld = pointPosition(this, this.ld);
        this.prd = pointPosition(this, this.rd);
    }
    this.genBombs = function(){
        this.genPosition();
        var array = bombPos[this.id%bombPos.length];
        var min ={};
        var max ={};
        var scale = {};

        if(this.plu.y < this.pru.y){
            min = this.plu;
            max = this.pru;
        }
        else{
            min = this.pru;
            max = this.plu;
        }
        scale.x = max.x-min.x;
        scale.y = max.y-min.y;
        min.x += scale.x*(1-bombSpace)/2;
        min.y += scale.y*(1-bombSpace)/2;
        scale.x *= bombSpace;
        scale.y *= bombSpace;

        for (var i = 0; i < array.length; i++) {
            var x = array[i]*scale.x + min.x;
            var y = array[i]*scale.y + min.y;

            new bombObject(x, y, this.angle);
            // console.log(bombs);
        };
    }
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





function drawLineImage(x, y, width, height, angle, color) { 
    var context = myGameArea.context;
    context.save(); 

    context.translate(x, y);
    context.rotate(angle * TO_RADIANS);
    if(color == null){
        height *=1.2;
        var currX, 
            padding = 1*unit, //remove padding between img
            marginEdge =20*unit; //display edge out of real line
        var edge = new Image();
        var line = new Image();
        line.src = path.line;
        var lineWidth = height*2*line.naturalWidth/line.naturalHeight;

        if(x < myGameArea.canvas.width/2){
            edge.src = path.lineEdgeR;
            edgeWidth = height*2*edge.naturalWidth/edge.naturalHeight;

            currX = width-edgeWidth+marginEdge;
            context.drawImage(edge, currX, -height, edgeWidth, height*2);

            while(currX > -width){
                currX -= lineWidth - padding;
                context.drawImage(line, currX, -height, lineWidth, height*2);
            }
        }
        else{
            edge.src = path.lineEdgeL;
            edgeWidth = height*2*edge.naturalWidth/edge.naturalHeight;

            currX = -width-marginEdge;
            context.drawImage(edge, currX, -height, edgeWidth, height*2);
            currX += edgeWidth - padding;

            while(currX < width){
                context.drawImage(line, currX, -height, lineWidth, height*2);
                currX += lineWidth - padding;
            }
        }
    }
    else{
        context.fillStyle = color;
        context.fillRect(-width, -height, width*2, height*2);
    }

    context.restore(); 
}