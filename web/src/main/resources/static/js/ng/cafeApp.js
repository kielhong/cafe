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
    .when("/articles/:articleId", {
            templateUrl : "/view/article.html",
            controller : "articleViewCtrl"
        })
    .otherwise({redirectTo:"/"})
});
