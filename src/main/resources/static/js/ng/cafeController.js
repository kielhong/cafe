var cafeApp = angular.module('cafeApp', ['ngRoute']);
cafeApp.config(function($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
    .when("/", {
        templateUrl : "/view/board.html",
        controller : "mainCtrl"
    })
    .when("/articles", {
        templateUrl : "/view/board.html",
        controller : "cafeArticleCtrl"
    })
    .when("/boards/:boardId", {
        templateUrl : "/view/board.html",
        controller : "boardArticleCtrl"
    })
    .when("/articles/post", {
        templateUrl : "/view/article_post.html",
        controller : "articlePostCtrl"
    })
    .when("/articles/:articleId", {
        templateUrl : "/view/article.html",
        controller : "articleViewCtrl"
    })
    .otherwise({redirectTo:"/"})
});

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
    $scope.tagEditMode = false;
    $scope.changeTagMode = function (mode) {
        $scope.tagEditMode = (mode == 'edit');
    };
    $scope.saveTag = function () {
        $scope.tags = [];
        $scope.tagNames.split(',').forEach(function(tagName) {
            tagName = tagName.trim();
            if (tagName != '' && !$scope.isDuplicateTag(tagName)) {
                $scope.tags.push({name : tagName});
            }
        });
        $scope.tag2tagNames();
        $scope.changeTagMode('list');
    }
    $scope.tag2tagNames = function() {
        var list = [];
        angular.forEach($scope.tags, function(tag) {
            list.push(tag.name);
        });
        $scope.tagNames = list.join(',');
    };
    $scope.isDuplicateTag = function(tagName) {
        for (var i = 0, len = $scope.tags.length; i < len; i++) {
            if ($scope.tags[i].name == tagName) {
                return true;
            }
        }
        return false;
    };

    $http.get("http://localhost:8080/api/cafes/" + $scope.$parent.cafeUrl + "/articles/" + $routeParams.articleId)
        .then(function(response) {
            $scope.article = response.data;
        });

    $http.get("http://localhost:8080/api/articles/" + $routeParams.articleId + "/comments?page=0&size=20")
        .then(function(response) {
            $scope.comments = response.data;
        });

    $http.get("http://localhost:8080/api/articles/" + $routeParams.articleId + "/tags")
        .then(function(response) {
            $scope.tags = response.data;
            $scope.tag2tagNames();
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


cafeApp.controller('articlePostCtrl', function($scope, $http, $location) {
    $http.get("http://localhost:8080/api/cafes/" + $scope.$parent.cafeUrl + "/boards")
        .then(function(response) {
            $scope.boards = response.data;
        });
    $scope.article = { title : '', content : ''};

    $scope.submit = function() {
        var url = "http://localhost:8080/api/cafes/" + $scope.$parent.cafeUrl + "/articles/";
        var data = $scope.article;
        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;',
            }
        };
        $(document).ready(function () {
                console.log('ready');
            });

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $http.defaults.headers.common[header] = token;

        $http.post(url, data, config)
            .then(
                function(response) {
                    $location.path("/articles/" + response.data.id);
                },
                function(response) {
                    if (response.status == 401 || response.status == 403) {
                        alert('게시물 작성 권한이 없습니다');
                    } else {
                        alert('게시물 작성에 실패했습니다')
                    }
                });
    };
});