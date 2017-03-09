var sectionApp = angular.module('sectionApp', ['ngRoute', 'ngResource']);

sectionApp.controller('storyCtrl', function($scope, $http, $filter) {
    $scope.toDateList = function(date) {
        return $filter('date')($scope.date, 'yyyy.MM.dd.').split('');
    };
    $scope.toDateString = function(date) {
        return date.toISOString().substring(0, 10);
    };
    $scope.displayDate = function(date) {
        $scope.dateList = $scope.toDateList(date);
    };
    $scope.prev = function() {
        $scope.date.setDate($scope.date.getDate() - 1);
        $scope.displayDate($scope.date);
    };
    $scope.next = function() {
        $scope.date.setDate($scope.date.getDate() + 1);
        $scope.displayDate($scope.date);

    };
    $scope.isLastDay = function() {
        return $scope.toDateString($scope.date) == $scope.toDateString(new Date());
    };

    $scope.dateStyle = function(n) {
        return (n == '.') ? "dot" : "n" + n + " _date";
    };
    $scope.nextDateStyle = function() {
        return $scope.isLastDay() ? "btn_next_off" : "btn_next";
    }

    $scope.date = new Date();
    $scope.displayDate($scope.date);
});

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

