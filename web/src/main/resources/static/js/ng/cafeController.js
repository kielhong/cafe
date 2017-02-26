cafeApp.controller('cafeCtrl', function($scope, $location, $http) {
    $scope.init = function() {
        $scope.cafeUrl = $location.absUrl().split('/').pop();

        $http.get("http://localhost:8080/cafes/" + $scope.cafeUrl)
            .then(function(response) {
                console.log(response);
                cafe = response.data;
                $scope.cafeId = cafe.id;
                $scope.cafeName = cafe.name;
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

cafeApp.controller('contentCtrl', function($scope, $http) {
    $scope.getContent = function(type, boardId) {
        $http.get("http://localhost:8080/cafes/" + $scope.$parent.cafeUrl + "/articles?page=0&size=10")
                .then(function(response) {
                    console.log(response);
            });
    };
});