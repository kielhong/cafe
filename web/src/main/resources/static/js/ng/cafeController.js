cafeApp.controller('cafeCtrl', function($scope, $location) {
    $scope.cafeUrl = $location.absUrl().split('/')[4];
    console.log($location.path());
});

cafeApp.controller('mainCtrl', function($scope, $http) {
    $scope.boardName = "전체글보기";

    $http.get("http://localhost:8080/cafes/" + $scope.$parent.cafeUrl + "/articles?page=0&size=10")
        .then(function(response) {
            console.log(response.data);
            $scope.articles = response.data;
        });
});

cafeApp.controller('cafeArticleCtrl', function($scope, $http) {
    $scope.boardName = "전체글보기";

    $http.get("http://localhost:8080/cafes/" + $scope.$parent.cafeUrl + "/articles?page=0&size=10")
        .then(function(response) {
            console.log(response.data);
            $scope.articles = response.data;
        });
});

cafeApp.controller('boardArticleCtrl', function($scope, $http, $routeParams) {
    console.log('boardArticleCtrl');
    $http.get("http://localhost:8080/cafes/" + $scope.$parent.cafeUrl + "/boards/" + $routeParams.boardId)
        .then(function(response) {
            $scope.boardName = response.data.name;
        });

    $http.get("http://localhost:8080/cafes/" + $scope.$parent.cafeUrl + "/boards/" + $routeParams.boardId + "/articles?page=0&size=10")
        .then(function(response) {
            $scope.articles = response.data;
        });
});

cafeApp.controller('articleViewCtrl', function($scope, $http, $routeParams) {
    $http.get("http://localhost:8080/cafes/" + $scope.$parent.cafeUrl + "/articles/" + $routeParams.articleId)
        .then(function(response) {
            $scope.article = response.data;
        });

    $http.get("http://localhost:8080/articles/" + $routeParams.articleId + "/comments?page=0&size=20")
        .then(function(response) {
            $scope.comments = response.data;
        });
});

cafeApp.controller('commentWriteCtrl', function($scope, $http, $routeParams) {
    $scope.submitComment = function() {
        if ($scope.text =='') {
            alert('내용을 입력하세요.')
        }
        var url = "http://localhost:8080/articles/" + $routeParams.articleId + "/comments";
        var data = {
            article: { id: $routeParams.articleId },
            commenter: { id: 1, nickname:'키엘' },
            comment: $scope.text
        };
        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        $http.post(url, data, config)
            .then(function(response) {
                $scope.comments.push(response.data);
            });
    };
});