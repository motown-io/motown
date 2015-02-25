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
package io.motown.mobieurope.destination.eventlisteners;

import io.motown.domain.api.chargingstation.NumberedTransactionId;
import io.motown.domain.api.chargingstation.TransactionStoppedEvent;
import io.motown.mobieurope.destination.entities.DestinationEndTransactionRequest;
import io.motown.mobieurope.destination.entities.DestinationNotifyRequestResultRequest;
import io.motown.mobieurope.destination.entities.TransactionData;
import io.motown.mobieurope.destination.persistence.entities.SourceEndpoint;
import io.motown.mobieurope.destination.persistence.repository.DestinationSessionRepository;
import io.motown.mobieurope.destination.persistence.repository.SourceEndpointRepository;
import io.motown.mobieurope.destination.service.SourceClient;
import io.motown.mobieurope.shared.persistence.entities.SessionInfo;
import io.motown.mobieurope.source.soap.schema.ServiceType;
import io.motown.mobieurope.source.soap.schema.TransactionStatus;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TransactionStoppedEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionStoppedEventListener.class);

    private DestinationSessionRepository destinationSessionRepository;

    private SourceEndpointRepository sourceEndpointRepository;

    @EventHandler
    protected void onEvent(TransactionStoppedEvent event) {
        LOG.info("Sending notifyRequestResult for a TransactionStoppedEvent to the source PMS");
        Integer transactionId = ((NumberedTransactionId) event.getTransactionId()).getNumber();

        SessionInfo sessionInfo = destinationSessionRepository.findSessionInfoByTransactionId(transactionId);
        sessionInfo.setTransactionId(transactionId);
        sessionInfo.getSessionStateMachine().eventStopOk();

        destinationSessionRepository.insertOrUpdateSessionInfo(sessionInfo);

        String requestIdentifier = sessionInfo.getRequestIdentifier();

        SourceEndpoint sourceEndpoint = sourceEndpointRepository.findSourceEndpointByPmsIdentifier(sessionInfo.getPmsIdentifier());
        SourceClient sourceClient = new SourceClient(sourceEndpoint.getSourceEndpointUrl());

        DestinationNotifyRequestResultRequest destinationNotifyRequestResultRequest = new DestinationNotifyRequestResultRequest(requestIdentifier);
        sourceClient.notifyRequestResult(destinationNotifyRequestResultRequest);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());

        try {
            XMLGregorianCalendar d1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            XMLGregorianCalendar d2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

            io.motown.mobieurope.source.soap.schema.TransactionData transactionData1 = new io.motown.mobieurope.source.soap.schema.TransactionData();
            transactionData1.setServiceType(ServiceType.EV_CHARGING);
            transactionData1.setTransactionStatus(TransactionStatus.TERMINATED);
            transactionData1.setStartTimestamp(d1);
            transactionData1.setEndTimestamp(d2);
            transactionData1.setEnergyConsumed(5.0);
            transactionData1.setLocalOperatorIdentifier(sessionInfo.getServicePms());
            transactionData1.setLocalServiceIdentifier(sessionInfo.getLocalServiceIdentifier());
            transactionData1.setHomeOperatorIdentifier(sessionInfo.getPmsIdentifier());
            transactionData1.setUserIdentifier(sessionInfo.getUserIdentifier());
            transactionData1.setAuthorizationIdentifier(sessionInfo.getAuthorizationIdentifier());

            TransactionData transactionData = new TransactionData(transactionData1);

            DestinationEndTransactionRequest destinationEndTransactionRequest = new DestinationEndTransactionRequest(sessionInfo.getAuthorizationIdentifier(), d1, d2, transactionData);

            sourceClient.endTransaction(destinationEndTransactionRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("Received a notifyRequestResultResponse from the source PMS");
    }

    public void setDestinationSessionRepository(DestinationSessionRepository destinationSessionRepository) {
        this.destinationSessionRepository = destinationSessionRepository;
    }

    public void setSourceEndpointRepository(SourceEndpointRepository sourceEndpointRepository) {
        this.sourceEndpointRepository = sourceEndpointRepository;
    }
}
