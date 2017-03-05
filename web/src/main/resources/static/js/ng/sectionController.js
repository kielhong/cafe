sectionApp.controller('contentCtrl', function($scope) {
    $scope.templateUrl = function() {
        switch ($scope.tab) {
            case 1:
                return '/view/section_subject_cafe.html';
            case 2:
                return '';
            case 3:
                return '/view/section_my_cafe.html'
            default:
                return '/view/section_subject_cafe.html';
        }
    };
    $scope.changeContent = function(tab) {
        console.log(tab);
        $('.t1').removeClass('on');
        $('.t2').removeClass('on');
        $('.t3').removeClass('on');

        $('.t' + tab).addClass('on');

        $scope.tab = tab;
        $scope.templateUrl();
    };
});

sectionApp.controller('categoryCtrl', function($scope, $http) {
    $http.get("http://localhost:8080/api/categories/")
        .then(function(response) {
            $scope.categories = response.data;
        });

    $scope.cafeByCategory = function(categoryId) {
        $http.get("http://localhost:8080/api/categories/" + categoryId + "/cafes")
            .then(function(response) {
                $scope.cafes = response.data;
        });
    };
});

sectionApp.controller('mycafeCtrl', function($scope, $http) {
    $('.content').addClass('no-after');
    $http.get("http://localhost:8080/api/members/my/cafes")
        .then(function(response) {
            $scope.mycafes = response.data;
        });

});