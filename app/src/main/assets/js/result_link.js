function getResultScores(resultScores){
  console.log(resultScores);
  angular.element(document.body).scope().$apply(function($scope){
    $scope.resultScores = resultScores;
  });
}