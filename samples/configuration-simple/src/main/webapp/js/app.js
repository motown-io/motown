/*
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
angular.module('demoApp', ['ngRoute', 'ngCookies', 'mgcrea.ngStrap.navbar', 'ui.bootstrap', 'demoApp.controllers', 'demoApp.services']).
    config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
        $httpProvider.defaults.headers.common['Accept'] = '*/*';
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/vnd.io.motown.operator-api-v1+json';

        $routeProvider.when('/login', {
            templateUrl: 'pages/login.html',
            controller: 'LoginController'
        });

        $routeProvider.when('/chargingstations', {
            templateUrl: 'pages/chargingstations.html'
        });

        $routeProvider.when('/configuration', {
            templateUrl: 'pages/configuration.html'
        });

        $routeProvider.when('/transactions', {
            templateUrl: 'pages/transactions.html'
        });

        $routeProvider.when('/mobieurope', {
            templateUrl: 'pages/mobieurope.html'
        });

        $routeProvider.otherwise({
            redirectTo: '/chargingstations'
        });

        // redirect browser to login page on HTTP status 401, otherwise show message
        $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
                return {
                    'responseError': function (rejection) {
                        var status = rejection.status;
                        var config = rejection.config;
                        var method = config.method;
                        var url = config.url;

                        if (status == 401) {
                            $location.path("/login");
                        } else if (status == 403) {
                            alert("You are not authorized to perform this action.");
                        } else {
                            $rootScope.error = method + " on " + url + " failed with status " + status;
                        }

                        return $q.reject(rejection);
                    }
                };
            }
        );

        // every request should contain the authToken if it exists.
        $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
            return {
                'request': function (config) {
//                    var isRestCall = config.url.indexOf('user') == 0;
                    var isRestCall = true;
                    //demoApp.useAuthTokenHeader = true;
                    var useAuthTokenHeader = true;

                    if (isRestCall && angular.isDefined($rootScope.authToken)) {
                        var authToken = $rootScope.authToken;
                        if (useAuthTokenHeader) {
                            config.headers['X-Auth-Token'] = authToken;
                        } else {
                            config.url = config.url + "?token=" + authToken;
                        }
                    }
                    return config || $q.when(config);
                }
            };
        });
    }]
).run(function ($rootScope, $location, $cookieStore) {
        $rootScope.$on('$viewContentLoaded', function () {
            delete $rootScope.error;
        });

        $rootScope.logout = function () {
            delete $rootScope.authToken;
            $cookieStore.remove('authToken');
            $location.path("/login");
        };

        // if there is no auth cookie we redirect to login
        var originalPath = $location.path();
        $location.path("/login");
        var authToken = $cookieStore.get('authToken');
        if (authToken !== undefined) {
            $rootScope.authToken = authToken;
            $location.path(originalPath);
        }
    }
);

var services = angular.module('demoApp.services', ['ngResource']);

services.factory('UserService', function ($resource) {
    return $resource('rest/user/:action', {},
        {
            authenticate: {
                method: 'POST',
                params: {'action': 'authenticate'},
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }
        }
    );
});
