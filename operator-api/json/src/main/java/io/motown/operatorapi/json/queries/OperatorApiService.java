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

import io.motown.domain.api.chargingstation.ConnectorId;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.entities.Transaction;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.operatorapi.viewmodel.persistence.repositories.TransactionRepository;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class OperatorApiService {

    private Random random;

    private CommandBus commandBus;

    private ChargingStationRepository repository;

    private TransactionRepository transactionRepository;

    public List<ChargingStation> findAllChargingStations() {
        return repository.findAll();
    }

    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Autowired
    public void setRandom(Random random) {
        this.random = random;
    }

    @Autowired
    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
