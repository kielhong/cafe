var sectionApp = angular.module('sectionApp', ['ngMessages']);

sectionApp.controller('cafeCreateCtrl', function($scope, $http, $window) {
    $scope.cafe = {
        visibility : 'PUBLIC',
        category : {
            id : '-1'
        }
    };

    $scope.checkUrl = function() {
        $http.get("http://localhost:8080/api/cafes/" + $scope.cafe.url)
            .then(
                function(response) {
                    $scope.frm.url.$error.unique = true;
                },
                function(response) {
                    if (response.status == 404) {
                        $scope.frm.url.$error.unique = false;
                    }
                });
    };

    $scope.submit = function() {
        if ($scope.cafe.category.id == '-1') {
            alert('카테고리를 선택해 주세요.');
            return;
        }
        console.log($scope.cafe);
        var url = "http://localhost:8080/api/cafes/";
        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;',
            }
        };
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $http.defaults.headers.common[header] = token;

        $http.post(url, $scope.cafe, config)
            .then(
                function(response) {
                    console.log(response.data);
                    $window.location.href = '/cafes/' + response.data.url + '/';
                },
                function(response) {
                    console.log(response.status);
                    if (response.status == 403) {
                        alert('카페 생성 권한이 없습니다');
                    } else {
                        alert('카페 생성에 실패했습니다')
                    }
                });
    };
});