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
function ChargingStationController($scope, $http, $timeout) {
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
            dataType: 'json',
            method: 'GET',
            data: '',
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*' // if this is not specified our request will fail
            }
        }).success(function(response) {
            $scope.chargingStations = response;
        });
    };

    $scope.registerChargingStation = function(chargingStation) {

        var cs = chargingStation;

        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            dataType: 'json',
            method: 'POST',
            data: ['Register',{
                'configuration' : {
                    'connectors' : [{
                        'connectorId' : 1,
                        'connectorType' : 'Type2',
                        'maxAmp' : 16
                    },{
                        'connectorId' : 2,
                        'connectorType' : 'Combo',
                        'maxAmp' : 32
                    }],
                    'settings' : {
                        'key':'value',
                        'key2':'value2'
                    }
                }
            }],
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*' // if this is not specified our request will fail
            }
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
            dataType: 'json',
            method: 'POST',
            data: ['ResetChargingStation',{
                'type': resetType
            }],
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        }).success(function(response) {
            console.log('reset requested');
        });
    };

    $scope.startTransaction = function(chargingStation) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            dataType: 'json',
            method: 'POST',
            data: ['RequestStartTransaction',{
                'connectorId': 1,
                'identifyingToken': 'TOKEN'
            }],
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        }).success(function(response) {
            console.log('start transaction requested');
        });
    };

    $scope.unlockConnector = function(chargingStation, connectorId) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            dataType: 'json',
            method: 'POST',
            data: ['UnlockConnector',{
                'connectorId': connectorId,
                'identifyingToken': 'TOKEN'
            }],
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        }).success(function(response) {
                console.log('unlock connector requested');
            });
    };

    $scope.dataTransfer = function(chargingStation, vendorId, messageId, data) {
        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            dataType: 'json',
            method: 'POST',
            data: ['DataTransfer',{
                'vendorId': vendorId,
                'messageId': messageId,
                'data': data
            }],
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        }).success(function(response) {
                console.log('data transfer requested');
            });
    };

    $scope.changeAvailability = function(chargingStation, type) {
        var availabilityType = 'operative';

        if (type == 'inoperative') {
            availabilityType = 'inoperative';
        }

        $http({
            url: 'charging-stations/' + chargingStation.id + '/commands',
            dataType: 'json',
            method: 'POST',
            data: ['ChangeAvailability',{
                'connectorId': 1,
                'availability': availabilityType
            }],
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        }).success(function(response) {
            console.log('change availability requested');
        });
    }

}