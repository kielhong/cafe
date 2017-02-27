cafeApp.controller('cafeCtrl', function($scope, $location, $http) {
    $scope.init = function() {
        console.log($location.absUrl().split('/'));
        cafeUrl = $location.absUrl().split('/')[4];

        $http.get("http://localhost:8080/cafes/" + cafeUrl)
            .then(function(response) {
                cafe = response.data;
                console.log(cafe);
                $scope.cafeId = cafe.id;
                $scope.cafeName = cafe.name;
                $scope.cafeUrl = cafe.url;
                $scope.createDateTime = cafe.createDateTime;
                $scope.cafeMemberCount = cafe.statistics.cafeMemberCount;
                $scope.cafeArticleCount = cafe.statistics.cafeArticleCount;
                $scope.boards = cafe.boards;
        });

        $scope.specialBoards = [
            { name: '카페태그보기', type: 'tag' },
            { name: '베스트게시물', type: 'list' },
            { name: '카페 캘린더', type: 'calendar' },
            { name: '카페북 책꽂이', type: 'book' }
        ];
    };
});

cafeApp.controller('mainCtrl', function($scope, $http) {
    console.log('cafeArticleCtrl');
    $scope.boardName = "전체글보기";

    $http.get("http://localhost:8080/cafes/" + $scope.$parent.cafeUrl + "/articles?page=0&size=10")
        .then(function(response) {
            console.log(response.data);
            $scope.articles = response.data;
        });
});

cafeApp.controller('cafeArticleCtrl', function($scope, $http) {
    console.log('cafeArticleCtrl');
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
    console.log('articleViewCtrl');
    $http.get("http://localhost:8080/cafes/" + $scope.$parent.cafeUrl + "/articles/" + $routeParams.articleId)
        .then(function(response) {
            $scope.article = response.data;
        });
});