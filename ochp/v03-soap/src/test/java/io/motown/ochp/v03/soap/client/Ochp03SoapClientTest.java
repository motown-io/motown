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

import com.google.common.collect.Lists;
import io.motown.domain.api.chargingstation.AuthorizationResultStatus;
import io.motown.ochp.util.DateFormatter;
import io.motown.ochp.v03.soap.schema.*;
import io.motown.ochp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ochp.viewmodel.persistence.entities.Identification;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import io.motown.ochp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ochp.viewmodel.persistence.repostories.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.motown.ochp.v03.soap.SOAPTestUtils.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class Ochp03SoapClientTest {

    private Echs echsClient;

    private Ochp03SoapClient client;

    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        OchpProxyFactory ochpClientProxyFactory = mock(OchpProxyFactory.class);
        echsClient = mock(Echs.class);
        when(ochpClientProxyFactory.createOchpService(anyString())).thenReturn(echsClient);
        when(echsClient.authenticate(any(AuthenticateRequest.class))).thenReturn(getAuthenticateSuccessResponse());

        transactionRepository = mock(TransactionRepository.class);

        ChargingStationRepository chargingStationRepository = mock(ChargingStationRepository.class);
        ChargingStation chargingStation = mock(ChargingStation.class);
        when(chargingStationRepository.findByChargingStationId(anyString())).thenReturn(chargingStation);
        client = new Ochp03SoapClient();
        client.setTransactionRepository(transactionRepository);
        client.setOchpProxyFactory(ochpClientProxyFactory);
        client.setChargingStationRepository(chargingStationRepository);
    }

    @Test
    public void verifyAuthenticationOnEachCall() {
        when(echsClient.getChargepointList(any(GetChargepointListRequest.class), anyString())).thenReturn(getChargepointListResponse());

        //Call twice in order to verify if authentication only takes place at first call
        client.getChargePointList();
        client.getChargePointList();

        verify(echsClient, times(2)).authenticate(any(AuthenticateRequest.class));
    }

    @Test
    public void testGetChargepointList() {
        when(echsClient.getChargepointList(any(GetChargepointListRequest.class), anyString())).thenReturn(getChargepointListResponse());

        List<ChargingStation> chargingStations = client.getChargePointList();

        assertNotNull(chargingStations);
    }

    @Test
    public void testGetRoamingAuthorizationList() {
        when(echsClient.getRoamingAuthorisationList(any(GetRoamingAuthorisationListRequest.class), anyString())).thenReturn(getRoamingAuthorisationListResponse());

        List<Identification> identifications = client.getRoamingAuthorizationList();

        assertNotNull(identifications);
    }

    @Test
    public void testGetTransactionList() {
        when(echsClient.getCDRs(any(GetCDRsRequest.class), anyString())).thenReturn(getCDRsResponse());

        List<Transaction> transactions = client.getTransactionList();

        assertNotNull(transactions);
    }

    @Test
    public void addCDRsVerifyTransactionToCDRInfoConversion() {
        when(echsClient.addCDRs(any(AddCDRsRequest.class), anyString())).thenReturn(getAddCDRsSuccessResponse());

        Date now = new Date();

        List<Transaction> transactions = Lists.newArrayList();
        Transaction transaction = new Transaction("transactionId");
        transaction.setEvseId("evseId");
        transaction.setIdentificationId("identificationId");
        transaction.setTransactionId("transactionId");
        transaction.setTimeStart(new Date(now.getTime() - TimeUnit.MINUTES.toMillis(80)));
        transaction.setTimeStop(now);
        transaction.setMeterStart(12);
        transaction.setMeterStop(34);

        ChargingStation chargingStation = mock(ChargingStation.class);
        transaction.setChargingStation(chargingStation);

        transactions.add(transaction);
        client.addChargeDetailRecords(transactions);

        ArgumentCaptor<AddCDRsRequest> addCDRsRequestArgument = ArgumentCaptor.forClass(AddCDRsRequest.class);
        verify(echsClient).addCDRs(addCDRsRequestArgument.capture(), anyString());
        CDRInfo firstCDRInfo = addCDRsRequestArgument.getValue().getCdrInfoArray().get(0);
        assertEquals(transaction.getEvseId(), firstCDRInfo.getEvseId());
        assertEquals(transaction.getIdentificationId(), firstCDRInfo.getAuthenticationId());
        assertEquals(transaction.getTransactionId(), firstCDRInfo.getCdrId());
        assertEquals(DateFormatter.toISO8601(transaction.getTimeStart()), firstCDRInfo.getStartDatetime());
        assertEquals(DateFormatter.formatDuration(transaction.getTimeStart(), transaction.getTimeStop()), firstCDRInfo.getDuration());
        assertEquals(DateFormatter.formatDuration(transaction.getTimeStart(), transaction.getTimeStop()), firstCDRInfo.getDuration());
        verify(transactionRepository, atLeast(1)).save(transactions);
        assertNotNull(transaction.getTimeSynced());
        //TODO: verify the rest of the parameters to see if they are correctly converted - Ingo Pak, 06 Mar 2014
    }

    @Test
    public void addCDRsVerifyNoSyncTimeStampUponFailure() {
        when(echsClient.addCDRs(any(AddCDRsRequest.class), anyString())).thenReturn(getAddCDRsFailedResponse());

        List<Transaction> transactions = Lists.newArrayList();
        Transaction transaction = new Transaction("transactionId");
        transaction.setChargingStation(new ChargingStation("chargingStationId"));
        transactions.add(transaction);

        client.addChargeDetailRecords(transactions);

        assertNull(transaction.getTimeSynced());
    }

    @Test
    public void sendRoamingAuthenticationList() {
        when(echsClient.setRoamingAuthorisationList(any(SetRoamingAuthorisationListRequest.class), anyString())).thenReturn(getSetRoamingAuthorisationListResponse());

        List<Identification> identifications = Lists.newArrayList();
        Identification identification = new Identification("idToken123", AuthorizationResultStatus.ACCEPTED);
        identifications.add(identification);
        client.sendAuthorizationInformation(identifications);

        ArgumentCaptor<SetRoamingAuthorisationListRequest> authorisationListRequestCaptor = ArgumentCaptor.forClass(SetRoamingAuthorisationListRequest.class);
        verify(echsClient).setRoamingAuthorisationList(authorisationListRequestCaptor.capture(), anyString());

        RoamingAuthorisationInfo firstRoamingAuthorisationInfo = authorisationListRequestCaptor.getValue().getRoamingAuthorisationInfoArray().get(0);
        assertEquals(identification.getIdentificationId(), firstRoamingAuthorisationInfo.getTokenId());
        assertEquals(1, firstRoamingAuthorisationInfo.getTokenActivated());
    }

    @Test
    public void testSendChargePointList() {
        when(echsClient.setChargepointList(any(SetChargepointListRequest.class), anyString())).thenReturn(getSetChargepointListResponse());

        List<ChargingStation> chargingStations = Lists.newArrayList();
        ChargingStation chargingStation = new ChargingStation(UUID.randomUUID().toString());
        chargingStations.add(chargingStation);
        client.sendChargePointList(chargingStations);

        ArgumentCaptor<SetChargepointListRequest> chargepointListRequestCaptor = ArgumentCaptor.forClass(SetChargepointListRequest.class);
        verify(echsClient).setChargepointList(chargepointListRequestCaptor.capture(), anyString());

        ChargepointInfo firstChargepointInfo = chargepointListRequestCaptor.getValue().getChargepointInfoArray().get(0);
        assertEquals(chargingStation.getChargingStationId(), firstChargepointInfo.getEvseId());
    }

}
