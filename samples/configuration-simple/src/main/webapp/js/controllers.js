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
angular.module('demoApp.controllers', []).
    controller('ChargingStationController',
        ['$scope', '$http', '$interval', function ($scope, $http, $interval) {
            $scope.init = function () {
                if(!$scope.chargingStationTimer) {
                    $scope.chargingStationTimer = $scope.startGetChargingStationsTimer();
                }
            };

            $scope.$on('$destroy', function destroy() {
                $interval.cancel($scope.chargingStationTimer);
                delete $scope.chargingStationTimer;
            });

            $scope.startGetChargingStationsTimer = function () {
                return $interval(function () {
                    $scope.getChargingStations();
                }, 2000);
            };

            $scope.getChargingStations = function () {
                $http({
                    url: 'rest/operator-api/charging-stations',
                    method: 'GET',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ''
                }).success(function (response) {
                    $scope.chargingStations = response;
                }).error(function () {
                    console.log('Error getting charging stations, cancel polling.');
                    $interval.cancel($scope.chargingStationTimer);
                    delete $scope.chargingStationTimer;
                });
            };

            $scope.registerChargingStation = function (chargingStation) {
                var cs = chargingStation;

                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['Register', {
                        'configuration': {
                            'evses': [
                                {
                                    'evseId': 1,
                                    'connectors': [
                                        {
                                            'maxAmp': 32,
                                            'phase': 3,
                                            'voltage': 240,
                                            'chargingProtocol': 'MODE3',
                                            'current': 'AC',
                                            'connectorType': 'TESLA'
                                        }
                                    ]
                                },
                                {
                                    'evseId': 2,
                                    'connectors': [
                                        {
                                            'maxAmp': 32,
                                            'phase': 3,
                                            'voltage': 240,
                                            'chargingProtocol': 'MODE3',
                                            'current': 'AC',
                                            'connectorType': 'TESLA'
                                        }
                                    ]
                                }
                            ],
                            'settings': {
                                'key': 'value',
                                'key2': 'value2'
                            }
                        }
                    }]
                }).success(function (response) {
                    console.log('registered');
                    cs.accepted = true;
                });
            };

            $scope.resetChargingStation = function (chargingStation, type) {
                var resetType = 'soft';

                if (type == 'hard') {
                    resetType = 'hard';
                }

                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['ResetChargingStation', {
                        'type': resetType
                    }]
                }).success(function (response) {
                    console.log('reset requested');
                });
            };

            $scope.startTransaction = function (chargingStation) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RequestStartTransaction', {
                        'evseId': 2,
                        'identifyingToken': {'token': 'TOKEN'}
                    }]
                }).success(function (response) {
                    console.log('start transaction requested');
                });
            };

            $scope.unlockEvse = function (chargingStation, evseId) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['UnlockEvse', {
                        'evseId': evseId,
                        'identifyingToken': {'token': 'TOKEN'}
                    }]
                }).success(function (response) {
                    console.log('unlock evse requested');
                });
            };

            $scope.dataTransfer = function (chargingStation, vendorId, messageId, data) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['DataTransfer', {
                        'vendorId': vendorId,
                        'messageId': messageId,
                        'data': data
                    }]
                }).success(function (response) {
                    console.log('data transfer requested');
                });
            };

            $scope.changeConfiguration = function (chargingStation, key, value) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['ChangeConfiguration', {
                        'key': key,
                        'value': value
                    }]
                }).success(function (response) {
                    console.log('change configuration requested');
                });
            };

            $scope.getDiagnostics = function (chargingStation, targetLocation) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RequestDiagnostics', {
                        'targetLocation': targetLocation
                    }]
                }).success(function (response) {
                    console.log('diagnostics requested');
                });
            };

            $scope.getConfiguration = function (chargingStation) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RequestGetConfiguration', {
                    }]
                }).success(function (response) {
                        console.log('get-configuration requested');
                    });
            };

            $scope.clearCache = function (chargingStation) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RequestClearCache', {
                    }]
                }).success(function (response) {
                    console.log('clear cache requested');
                });
            };

            $scope.updateFirmware = function (chargingStation, location, retrieveDate) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['UpdateFirmware', {
                        'location': location,
                        'retrieveDate': retrieveDate
                    }]
                }).success(function (response) {
                    console.log('clear cache requested');
                });
            };

            $scope.getAuthorizationListVersion = function (chargingStation) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RequestAuthorizationListVersion', {
                    }]
                }).success(function (response) {
                    console.log('clear cache requested');
                });
            };

            $scope.sendAuthorizationList = function (chargingStation, listVersion, updateType, items) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['SendAuthorizationList', {
                        'listVersion': listVersion,
                        'updateType': updateType,
                        'items': items
                    }]
                }).success(function (response) {
                    console.log('clear cache requested');
                });
            };

            $scope.changeAvailability = function (chargingStation, type) {
                var availabilityType = 'operative';

                if (type == 'inoperative') {
                    availabilityType = 'inoperative';
                }

                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['ChangeAvailability', {
                        'evseId': 1,
                        'availability': availabilityType
                    }]
                }).success(function (response) {
                    console.log('change availability requested');
                });
            };

            $scope.reserveNow = function (chargingStation, evseId, identifyingToken, expiryDate) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RequestReserveNow', {
                        'evseId': evseId,
                        'identifyingToken': identifyingToken,
                        'expiryDate': expiryDate
                    }]
                }).success(function (response) {
                    console.log('reserve now requested');
                });
            };

            $scope.cancelReservation = function (chargingStation, reservationId) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RequestCancelReservation', {
                        'reservationId': reservationId
                    }]
                }).success(function (response) {
                        console.log('cancel reservation requested');
                    });
            };

            $scope.updateReservable = function (chargingStation, reservable) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['UpdateChargingStationReservable', {
                        'reservable': reservable
                    }]
                }).success(function (response) {
                    console.log('update reservable requested');
                });
            };

            $scope.placeChargingStation = function (chargingStation, coordinates, address, accessibility) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['PlaceChargingStation', {
                        'coordinates': coordinates,
                        'address': address,
                        'accessibility': accessibility
                    }]
                }).success(function (response) {
                    console.log('charging station placed');
                });
            };

            $scope.improveChargingStationLocation = function (chargingStation, coordinates, address, accessibility) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['ImproveChargingStationLocation', {
                        'coordinates': coordinates,
                        'address': address,
                        'accessibility': accessibility
                    }]
                }).success(function (response) {
                    console.log('charging station location improved');
                });
            };

            $scope.moveChargingStation = function (chargingStation, coordinates, address, accessibility) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['MoveChargingStation', {
                        'coordinates': coordinates,
                        'address': address,
                        'accessibility': accessibility
                    }]
                }).success(function (response) {
                    console.log('charging station moved');
                });
            };

            $scope.setOpeningTimes = function (chargingStation, openingTimes) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['SetChargingStationOpeningTimes', {
                        'openingTimes': openingTimes
                    }]
                }).success(function (response) {
                    console.log('Opening times set');
                });
            };

            $scope.addOpeningTimes = function (chargingStation, openingTimes) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['AddChargingStationOpeningTimes', {
                        'openingTimes': openingTimes
                    }]
                }).success(function (response) {
                    console.log('Opening times added');
                });
            };

            $scope.grantPermission = function (chargingStation, commandClass, userIdentity) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['GrantPermission', {
                        'commandClass': commandClass,
                        'userIdentity': userIdentity

                    }]
                }).success(function (response) {
                    console.log('Permission requested');
                });
            };

            $scope.revokePermission = function (chargingStation, commandClass, userIdentity) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStation.id + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RevokePermission', {
                        'commandClass': commandClass,
                        'userIdentity': userIdentity

                    }]
                }).success(function (response) {
                    console.log('Revoke permission requested');
                });
            }
        }]).
    controller('TransactionController',
        ['$scope', '$http', '$interval', function ($scope, $http, $interval) {
            $scope.init = function () {
                if(!$scope.transactionTimer) {
                    $scope.transactionTimer = $scope.startGetTransactionsTimer();
                }
            };

            $scope.$on('$destroy', function destroy() {
                console.log('Cancelling timer');
                $interval.cancel($scope.transactionTimer);
                delete $scope.transactionTimer;
            });

            $scope.startGetTransactionsTimer = function () {
                return $interval(function () {
                    $scope.getTransactions();
                }, 2000);
            };

            $scope.getTransactions = function () {
                $http({
                    url: 'rest/operator-api/transactions',
                    method: 'GET',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ''
                }).success(function (response) {
                    $scope.transactions = response;
                }).error(function() {
                    console.log('Error getting transactions, cancel polling.');
                    $interval.cancel($scope.transactionTimer);
                    delete $scope.transactionTimer;
                });
            };

            $scope.stopTransaction = function (chargingStationId, id) {
                $http({
                    url: 'rest/operator-api/charging-stations/' + chargingStationId + '/commands',
                    method: 'POST',
                    headers: {'Content-Type': 'application/vnd.io.motown.operator-api-v1+json'},
                    data: ['RequestStopTransaction', {
                        'id': id
                    }]
                }).success(function (response) {
                    console.log('remote stop!');
                });
            };
        }]).
    controller('ConfigurationController',
        ['$scope', '$http', '$interval', function ($scope, $http, $interval) {
            $scope.init = function () {
                $scope.getChargingStationTypes();
            };

            $scope.getChargingStationTypes = function () {
                $http({
                    url: 'rest/config/chargingstationtypes',
                    method: 'GET',
                    headers: {'Content-Type': 'application/vnd.io.motown.charging-station-configuration-api-v1+json'},
                    data: ''
                }).success(function (response) {
                    $scope.chargingStationTypes = response;
                });
            };
        }]
    ).
    controller('LoginController',
        ['$scope', '$rootScope', '$location', '$cookieStore', 'UserService', function ($scope, $rootScope, $location, $cookieStore, UserService) {
            $scope.rememberMe = false;

            $scope.login = function() {
                UserService.authenticate($.param({username: $scope.username, password: $scope.password}), function(authenticationResult) {
                    var authToken = authenticationResult.token;
                    $rootScope.authToken = authToken;
                    if ($scope.rememberMe) {
                        $cookieStore.put('authToken', authToken);
                    }
                    $location.path("/");
                }, function() {
                    alert('Combination username & password not valid.');
                });
            };
        }
    ]).
    controller('HeaderController',
        ['$scope', '$location', function($scope, $location) {
            $scope.isActive = function (viewLocation) {
                return viewLocation === $location.path();
            };
        }]
    );
