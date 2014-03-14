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

package io.motown.ochp.v03.soap.client;

import io.motown.ochp.viewmodel.persistence.TransactionStatus;
import io.motown.ochp.viewmodel.persistence.entities.Identification;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import io.motown.ochp.viewmodel.persistence.repostories.IdentificationRepository;
import io.motown.ochp.viewmodel.persistence.repostories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OchpSchedulingService {

    private static final Logger LOG = LoggerFactory.getLogger(OchpSchedulingService.class);

    private Ochp03SoapClient ochp03SoapClient;

    private TransactionRepository transactionRepository;

    private IdentificationRepository identificationRepository;

    public void executeGetChargingStationList() {
        ochp03SoapClient.getChargePointList();
    }

    public void executeAddCDRs() {
        LOG.info("Executing scheduled task addCDRs");

        List<Transaction> completedTransactions = transactionRepository.findTransactionsByStatus(TransactionStatus.STOPPED);
        if(completedTransactions != null && !completedTransactions.isEmpty()){
            ochp03SoapClient.addChargeDetailRecords(completedTransactions);

            //TODO: Probably have to mark the sent transactions as synchronized - Ingo Pak, 07 Mar 2014

            LOG.info("Added {} CDR(s)", completedTransactions.size());
        } else {
            LOG.info("No action required, there are no new CDR(s) to add");
        }
    }

    public void executeSetRoamingAuthorizationList() {
        LOG.info("Executing set roaming authorisation list");

        List<Identification> identifications = identificationRepository.all();

        ochp03SoapClient.sendAuthorizationInformation(identifications);
    }

    public void setOchp03SoapClient(Ochp03SoapClient ochp03SoapClient) {
        this.ochp03SoapClient = ochp03SoapClient;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
