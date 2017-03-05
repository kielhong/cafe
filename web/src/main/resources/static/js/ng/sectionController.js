sectionApp.controller('categoryCtrl', function($scope) {
    $scope.cafeByCategory = function(categoryId) {
        $scope.$parent.$broadcast('cafe_by_category', {
            id: categoryId
        });
    };

    $scope.$on('initCategoryCafeCtrl', function(event, args) {
        $scope.cafeByCategory(1);
    });
});

sectionApp.controller('categoryCafeCtrl', function($scope, $http) {
    $scope.$on('cafe_by_category', function(event, category) {
        $http.get("http://localhost:8080/api/categories/" + category.id + "/cafes")
            .then(function(response) {
                $scope.cafes = response.data;
        });
    });

    $scope.$parent.$broadcast('initCategoryCafeCtrl', null);
});

