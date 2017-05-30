/**
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
package io.motown.operatorapi.json.queries;

import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.entities.Reservation;
import io.motown.operatorapi.viewmodel.persistence.entities.Transaction;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.operatorapi.viewmodel.persistence.repositories.ReservationRepository;
import io.motown.operatorapi.viewmodel.persistence.repositories.TransactionRepository;

import java.util.List;

public class OperatorApiService {

    private ChargingStationRepository repository;

    private TransactionRepository transactionRepository;

    private ReservationRepository reservationRepository;

    public List<ChargingStation> findAllChargingStations(int offset, int limit) {
        return repository.findAll(offset, limit);
    }

    public Long getTotalNumberOfChargingStations() {
        return repository.getTotalNumberOfChargingStations();
    }

    public List<Transaction> findAllTransactions(int offset, int limit) {
        return transactionRepository.findAll(offset, limit);
    }

    public List<Reservation> findAllReservations(int offset, int limit) {
        return reservationRepository.findAll(offset, limit);
    }
    
    public Long getTotalNumberOfTransactions() {
        return transactionRepository.getTotalNumberOfTransactions();
    }

    public Long getTotalNumberOfReservations() {
        return reservationRepository.getTotalNumberOfReservations();
    }

    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
