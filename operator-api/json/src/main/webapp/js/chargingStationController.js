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
function ChargingStationController($scope, $http, $timeout, $httpProvider) {
    $httpProvider.defaults.headers.common['Content-Type'] = 'application/json';
    $httpProvider.defaults.headers.common['Accept'] = '*/*';

    $scope.init = function() {
        $scope.startGetChargingStationsTimer();
    };

    $scope.startGetChargingStationsTimer = function() {
        $timeout(function() {
            $scope.getChargingStations();
            $scope.startGetChargingStationsTimer();
        }, 1000);
    };

    $scope.getChargingStations = function() {
        $http({
            url: 'charging-stations',
            method: 'GET',
            data: ''
        }).success(function(response) {
            $scope.chargingStations = response;
        });
    };

    $scope.registerChargingStation = function(chargingStation) {

        var cs = chargingStation;

        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['Register',{
                'configuration' : {
                    'evses' : [{
                        'evseId' : 1,
                        'connectors' : [{
                            'maxAmp': 32,
                            'phase': 3,
                            'voltage': 230,
                            'chargingProtocol': 'MODE3',
                            'current': 'AC',
                            'connectorType': 'TESLA'
                        }]
                    },{
                        'evseId' : 2,
                        'connectors' : [{
                            'maxAmp': 32,
                            'phase': 3,
                            'voltage': 230,
                            'chargingProtocol': 'MODE3',
                            'current': 'AC',
                            'connectorType': 'TESLA'
                        }]
                    }],
                    'settings' : {
                        'key':'value',
                        'key2':'value2'
                    }
                }
            }]
        }).success(function(response) {
            console.log('registered');
            cs.accepted = true;
        });
    };

    $scope.resetChargingStation = function(chargingStation, type) {
        var resetType = 'soft';

        if (type == 'hard') {
            resetType = 'hard';
        }

        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['ResetChargingStation',{
                'type': resetType
            }]
        }).success(function(response) {
            console.log('reset requested');
        });
    };

    $scope.startTransaction = function(chargingStation) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['RequestStartTransaction',{
                'evseId': 1,
                'identifyingToken': {'token': 'TOKEN'}
            }]
        }).success(function(response) {
            console.log('start transaction requested');
        });
    };

    $scope.unlockEvse = function(chargingStation, evseId) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['UnlockEvse',{
                'evseId': evseId,
                'identifyingToken': {'token': 'TOKEN'}
            }]
        }).success(function(response) {
            console.log('unlock evse requested');
        });
    };

    $scope.dataTransfer = function(chargingStation, vendorId, messageId, data) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['DataTransfer',{
                'vendorId': vendorId,
                'messageId': messageId,
                'data': data
            }]
        }).success(function(response) {
                console.log('data transfer requested');
            });
    };

    $scope.changeConfiguration = function(chargingStation, key, value) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['ChangeConfiguration',{
                'key': key,
                'value': value
            }]
        }).success(function(response) {
                console.log('change configuration requested');
            });
    };

    $scope.getDiagnostics = function(chargingStation, targetLocation) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['RequestDiagnostics',{
                'targetLocation': targetLocation
            }]
        }).success(function(response) {
                console.log('diagnostics requested');
            });
    };

    $scope.clearCache = function(chargingStation) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['ClearCache',{
            }]
        }).success(function(response) {
                console.log('clear cache requested');
            });
    };

    $scope.updateFirmware = function(chargingStation, location, retrieveDate) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['UpdateFirmware',{
                'location': location,
                'retrieveDate': retrieveDate
            }]
        }).success(function(response) {
                console.log('clear cache requested');
            });
    };

    $scope.getAuthorizationListVersion = function(chargingStation) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['RequestAuthorizationListVersion',{
            }]
        }).success(function(response) {
                console.log('clear cache requested');
            });
    };

    $scope.sendAuthorizationList = function(chargingStation, listVersion, updateType, items) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['SendAuthorizationList',{
                'listVersion': listVersion,
                'updateType': updateType,
                'items': items
            }]
        }).success(function(response) {
                console.log('clear cache requested');
            });
    };

    $scope.changeAvailability = function(chargingStation, type) {
        var availabilityType = 'operative';

        if (type == 'inoperative') {
            availabilityType = 'inoperative';
        }

        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['ChangeAvailability',{
                'evseId': 1,
                'availability': availabilityType
            }]
        }).success(function(response) {
            console.log('change availability requested');
        });
    };

    $scope.reserveNow = function(chargingStation, evseId, identifyingToken) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['RequestReserveNow',{
                'evseId': evseId,
                'identifyingToken': identifyingToken,
                'expiryDate': new Date()
            }]
        }).success(function(response) {
            console.log('change availability requested');
        });
    };

    $scope.updateReservable = function(chargingStation, reservable) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            method: 'POST',
            data: ['UpdateChargingStationReservable',{
                'reservable': reservable
            }]
        }).success(function(response) {
            console.log('update reservable requested');
        });
    };

}