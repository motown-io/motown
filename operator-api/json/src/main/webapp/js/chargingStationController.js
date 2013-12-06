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
}