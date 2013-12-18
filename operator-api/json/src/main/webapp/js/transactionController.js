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
