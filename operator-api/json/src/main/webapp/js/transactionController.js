function TransactionController($scope, $http, $timeout) {
    $scope.init = function() {
        $scope.startGetTransactionsTimer();
    };

    $scope.startGetTransactionsTimer = function() {
        $timeout(function() {
            $scope.getTransactions();
            $scope.startGetTransactionsTimer();
        }, 1000);
    };

    $scope.getTransactions = function() {
        $http({
            url: 'transactions',
            dataType: 'json',
            method: 'GET',
            data: '',
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*'
            }
        }).success(function(response) {
            $scope.transactions = response;
        });
    };

    $scope.stopTransaction = function(chargingStationId, id) {
        $http({
            url: 'charging-stations/' + chargingStationId + '/commands',
            dataType: 'json',
            method: 'POST',
            data: ['RequestStopTransaction',{
                'transactionId': id
            }],
            headers: {
                'Content-Type': 'application/json',
                'Accept': '*/*' // if this is not specified our request will fail
            }
        }).success(function(response) {
            console.log('remote stop!');
        });
    }

}
