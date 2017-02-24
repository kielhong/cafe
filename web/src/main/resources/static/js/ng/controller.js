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

    $scope.cafeByCategory = function(categoryId) {
        $scope.$parent.$broadcast('cafe_by_category', {
            id: categoryId
        });
    };

    $scope.isFirst = function(first, category) {
      if (first) {
        $scope.cafeByCategory(category.id);
      }
    }
});

app2.controller('categoryCafeCtrl', function($scope, $http) {
    $scope.$on('cafe_by_category', function(event, category) {
        $http.get("http://localhost:8080/categories/" + category.id + "/cafes")
            .then(function(response) {
                $scope.cafes = response.data;
        });
    });
});

