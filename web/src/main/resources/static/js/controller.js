app.controller('usersController', function($scope) {
    $scope.headingTitle = "User List";
});

app.controller('rolesController', function($scope) {
    $scope.headingTitle = "Roles List";
});

app.controller('categoriesController', function($scope, $http){
    $http.get("http://localhost:8080/categories")
        .then(function(response) {
        $scope.categories = response.data;
    });
});

app2.controller('categoryCtrl', function($scope, $http){
    $scope.init = function() {
        $http.get("http://localhost:8080/categories")
                .then(function(response) {
                $scope.categories = response.data;
            });
    };
});