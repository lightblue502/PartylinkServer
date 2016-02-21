function getIpAddress(ipAddress){
  console.log(ipAddress);
  angular.element(document.body).scope().$apply(function($scope){
          $scope.display = "IP: "+ipAddress;
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
          $scope.display = "loading... "+times;
  });
};

function getPlayerSize(now, size){
  angular.element(document.body).scope().$apply(function($scope){
          $scope.connect = "Connected "+now+" player";
          if(now>1) $scope.connect +="s";
          $scope.connect +=".";

          if(now == size)
          	$scope.ip = "animated zoomOutUp";
  });
};