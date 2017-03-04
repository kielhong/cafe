var app = angular.module('app', ['ngRoute','ngResource']);
app.config(function($routeProvider,$locationProvider){
    $locationProvider.hashPrefix('');
    $routeProvider
        .when('/categories', {
            templateUrl: '/categories.html',
            controller: 'categoriesController'
         })
        .otherwise(
            { redirectTo: '/'}
        );
});

var sectionApp = angular.module('sectionApp', ['ngRoute', 'ngResource']);
