function getIpAddress(ipAddress){
  console.log(ipAddress);
  angular.element(document.body).scope().$apply(function($scope){
          $scope.display = "IP: "+ipAddress;
  });
};

function loading(times){
  console.log(times);
  angular.element(document.body).scope().$apply(function($scope){
          $scope.display = "loading... "+times;
  });
};