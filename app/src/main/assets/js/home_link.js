function getIpAddress(ipAddress){
  console.log(ipAddress);
  angular.element(document.body).scope().$apply(function($scope){
          if(ipAddress == "0.0.0.0")
             $scope.display = "Room Wrong .."
          new QRCode(document.getElementById("qrcode"), ipAddress);
          $scope.ip = "animated pulse infinite";

  });
};

function setDisplay(text){
  angular.element(document.body).scope().$apply(function($scope){
          $scope.display = text;
  });
}

function loading(times){
  console.log(times);
  angular.element(document.body).scope().$apply(function($scope){
          $scope.connect = "Loading... "+times;
  });
};

function getPlayerSize(now, size){
  angular.element(document.body).scope().$apply(function($scope){
          $scope.connect = "Waiting for "+(size-now)+" player"; 
          if(size-now>1) $scope.connect +="s";
          $scope.connect +=".";

          if(now==0) //animate when call fn
            $scope.conClass = " animated fadeInDown";
          else{
            var anim = ["flipInY","zoomIn","bounceIn"];
            var r = Math.floor(Math.random()*3);
            $scope.conClass = " animated "+anim[r]; 
          }

          if(now == size){ //animate if full
            $scope.ip = "animated zoomOutUp";
            $scope.conClass = "animated tada";
          	$scope.connect = "READY!";
          }
          else{ //animate standby normal
            $scope.timeoutFn(function() { 
              $scope.conClass = " animated infinite pulse"; 
            },800);
          }
  });
};