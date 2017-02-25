var app = angular.module('app', ['ngRoute','ngResource']);
app.config(function($routeProvider,$locationProvider){
    $locationProvider.hashPrefix('');
    $routeProvider
        .when('/categories', {
            templateUrl: '/categories.html',
            controller: 'categoriesController'
         })
        .when('/users',{
            templateUrl: '/users.html',
            controller: 'usersController'
        })
        .when('/roles',{
            templateUrl: '/views/roles.html',
            controller: 'rolesController'
        })
        .otherwise(
            { redirectTo: '/'}
        );
});

var app2 = angular.module('sectionApp', ['ngRoute', 'ngResource']);
