app.controller('cafeCtrl', function($scope, $location, $http) {
    $scope.init = function() {
        url = $location.absUrl().split('/').pop();

        $http.get("http://localhost:8080/cafes/" + url)
            .then(function(response) {
                console.log(response);
                cafe = response.data;
                $scope.cafeName = cafe.name;
                $scope.createDateTime = cafe.createDateTime;
                $scope.cafeMemberCount = cafe.statistics.cafeMemberCount;
        });
    };
});