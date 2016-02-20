var __backdoor_div__;
function creatBD() {
	var style = "background:black; color:white; font-size:3vh; z-index:99; "
	style += "position:absolute; display:inline; padding:1vh; margin:2vh; visibility: hidden;";
	style += "border-radius: 1vh;";
	document.body.innerHTML += "<div id='__backdoor_div__' style=\""+style+"\" ></div>";
	__backdoor_div__ = document.getElementById('__backdoor_div__');
	
}

document.onkeypress = function (button) {
// document.onmousedown = function(){
	if(__backdoor_div__ == null)
		creatBD();

	var key = button.charCode - 48; //0-9
	// var key = 1;
	if (0<=key && key<=9){
 		console.log("Press NUMPAD : "+key);

 		__backdoor_div__.style.visibility = "visible";
 		__backdoor_div__.innerHTML = "Press NUMPAD : "+key;
 		__backdoor_div__.className = "";
 		setTimeout(function() {
 			__backdoor_div__.className = "animated fadeOut";
 		}, 1000);

 		Android.backDoorKey(key+"");
 	}

}