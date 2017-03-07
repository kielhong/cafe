cafeApp.controller('cafeCtrl', function($scope, $location) {
    $scope.cafeUrl = $location.absUrl().split('/')[4];
});

cafeApp.controller('mainCtrl', function($scope, $http) {
    $scope.boardName = "전체글보기";

    $http.get("http://localhost:8080/api/cafes/" + $scope.$parent.cafeUrl + "/articles?page=0&size=10")
        .then(function(response) {
            $scope.articles = response.data;
        });
});

cafeApp.controller('cafeArticleCtrl', function($scope, $http) {
    $scope.boardName = "전체글보기";

    $http.get("http://localhost:8080/api/cafes/" + $scope.$parent.cafeUrl + "/articles?page=0&size=10")
        .then(function(response) {
            $scope.articles = response.data;
        });
});

cafeApp.controller('boardArticleCtrl', function($scope, $http, $routeParams) {
    $http.get("http://localhost:8080/api/cafes/" + $scope.$parent.cafeUrl + "/boards/" + $routeParams.boardId)
        .then(function(response) {
            $scope.boardName = response.data.name;
        });

    $http.get("http://localhost:8080/api/cafes/" + $scope.$parent.cafeUrl + "/boards/" + $routeParams.boardId + "/articles?page=0&size=10")
        .then(function(response) {
            $scope.articles = response.data;
        });
});

cafeApp.controller('articleViewCtrl', function($scope, $http, $routeParams) {
    $http.get("http://localhost:8080/api/cafes/" + $scope.$parent.cafeUrl + "/articles/" + $routeParams.articleId)
        .then(function(response) {
            $scope.article = response.data;
        });

    $http.get("http://localhost:8080/api/articles/" + $routeParams.articleId + "/comments?page=0&size=20")
        .then(function(response) {
            $scope.comments = response.data;
        });
});

cafeApp.controller('commentWriteCtrl', function($scope, $http, $routeParams) {
    $scope.submitComment = function() {
        if ($scope.text == "") {
            alert('내용을 입력하세요.')
        }
        var url = "http://localhost:8080/api/articles/" + $routeParams.articleId + "/comments";
        var data = {
            comment: $scope.text
        };
        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;',
            }
        };
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $http.defaults.headers.common[header] = token;

        $http.post(url, data, config)
            .then(
                function(response) {
                    $scope.comments.push(response.data);
                },
                function(response) {
                    if (response.status == 403) {
                        alert('댓글 작성 권한이 없습니다');
                    } else {
                        alert('댓글 작성에 실패했습니다')
                    }
                });
    };
});