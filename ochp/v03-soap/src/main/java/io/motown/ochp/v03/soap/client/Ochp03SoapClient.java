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
import io.motown.ochp.viewmodel.ochp.Ochp03Client;
import io.motown.ochp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ochp.viewmodel.persistence.entities.Identification;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import io.motown.ochp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ochp.viewmodel.persistence.repostories.TransactionRepository;
import org.apache.cxf.interceptor.security.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class Ochp03SoapClient implements Ochp03Client {

    private static final Logger LOG = LoggerFactory.getLogger(Ochp03SoapClient.class);

    private static final int ACCEPTED = 0;

    private static final int DEACTIVATED = 0;
    private static final int ACTIVATED = 1;

    private OchpProxyFactory ochpProxyFactory;

    private ChargingStationRepository chargingStationRepository;

    private TransactionRepository transactionRepository;

    private String serverAddress;

    private String username;

    private String password;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Performs authentication
     * @return String holding the authenticationToken which is valid for a limited amount of time (<1hr)
     */
    private String authenticate() {
        LOG.info("Authenticating");

        Echs ochpClientService = this.createOchpClientService();
        AuthenticateRequest request = new AuthenticateRequest();
        request.setUserId(username);
        request.setPassword(password);
        AuthenticateResponse response = ochpClientService.authenticate(new AuthenticateRequest());

        if (response.getResultCode() != ACCEPTED) {
            LOG.error("Authentication of {} failed", username);
            throw new AuthenticationException("Authentication failed");
        } else {
            LOG.info("Authentication of {} successfull", username);
        }
        return response.getAuthToken();
    }

    @Override
    public void addChargeDetailRecords(List<Transaction> transactionList) {
        LOG.info("Add CDRs");
        AddCDRsRequest request = new AddCDRsRequest();
        List<CDRInfo> cdrInfoList = request.getCdrInfoArray();
        for(Transaction transaction : transactionList){
            CDRInfo cdrInfo = new CDRInfo();
            cdrInfo.setAuthenticationId(transaction.getIdentificationId());
            cdrInfo.setCdrId(transaction.getTransactionId());
            cdrInfo.setEvseId(transaction.getEvseId());
            cdrInfo.setDuration(DateFormatter.formatDuration(transaction.getTimeStart(), transaction.getTimeStop()));
            cdrInfo.setStartDatetime(DateFormatter.toISO8601(transaction.getTimeStart()));
            cdrInfo.setEndDatetime(DateFormatter.toISO8601(transaction.getTimeStop()));
            cdrInfo.setVolume(String.format("%.4f", transaction.calculateVolume()));

            ChargingStation chargingStation = transaction.getChargingStation();
            if(chargingStation != null) {
                cdrInfo.setChargePointId(chargingStation.getChargingStationId());
                cdrInfo.setChargePointAddress(chargingStation.getAddress());
                cdrInfo.setChargePointCity(chargingStation.getCity());
                cdrInfo.setChargePointZip(chargingStation.getPostalCode());
                cdrInfo.setChargePointCountry(chargingStation.getCountry());
            }
            // TODO: Decide if the fields below will be provided - Ingo Pak, 11 Mar 2014
            cdrInfo.setChargePointType("");
            cdrInfo.setInfraProviderId("");
            //identification of the physical energy meter
            cdrInfo.setMeterId("");
            //object identification of the register in the meter (IEC 62056-61 eg. 1-1:1.8.0)
            cdrInfo.setObisCode("");
            //identifies the type of product that is delivered
            cdrInfo.setProductType("");
            cdrInfo.setTariffType("");
            cdrInfo.setServiceProviderId("");
            //identifies a customer in the electric mobility charging context (http://data.fir.de/projektseiten/emobility-ids/)
            cdrInfo.setEvcoId("");

            cdrInfoList.add(cdrInfo);
        }

        AddCDRsResponse response = this.createOchpClientService().addCDRs(request, authenticate());

        if(response.getResult() == null || response.getResult().getResultCode() != ACCEPTED) {
            LOG.error("Failed to add the CDR's: {}", response.getResult().getResultDescription());
        } else {
            LOG.info("Successfully added CDR's, marking them synced");
            Date syncTime = new Date();
            for (Transaction transaction : transactionList) {
                transaction.setTimeSynced(syncTime);
            }
            transactionRepository.save(transactionList);
        }
    }

    /**
     * Sends the complete list of known identification tokens
     * @param identifications
     */
    @Override
    public void sendAuthorizationInformation(List<Identification> identifications) {
        LOG.info("Send authorisation list");
        SetRoamingAuthorisationListRequest request = new SetRoamingAuthorisationListRequest();
        List<RoamingAuthorisationInfo> roamingAuthorisationList = request.getRoamingAuthorisationInfoArray();

        for (Identification identification : identifications){
            RoamingAuthorisationInfo authorisationInfo = new RoamingAuthorisationInfo();
            //TODO: We do not have the EvcoId/ContractId information available - Ingo Pak, 20 Mar 2014
            authorisationInfo.setEvcoId("NL-G00-000252-10");
            authorisationInfo.setTokenId(identification.getIdentificationId());
            if(AuthorizationResultStatus.ACCEPTED.equals(identification.getAuthorizationStatus())) {
                authorisationInfo.setTokenActivated(ACTIVATED);
            } else {
                authorisationInfo.setTokenActivated(DEACTIVATED);
            }
            //TODO: We do not have the available information for the fields below, these are hardcoded for now - Ingo Pak, 20 Mar 2014
            authorisationInfo.setPinMandatory(0);
            authorisationInfo.setPin(0);
            authorisationInfo.setHash("");
            authorisationInfo.setRoamingHubId(1);
            authorisationInfo.setPrintedNumber("");
            authorisationInfo.setExpiryDate(null);

            roamingAuthorisationList.add(authorisationInfo);
        }

        SetRoamingAuthorisationListResponse response = this.createOchpClientService().setRoamingAuthorisationList(request, authenticate());

        if(response.getResult() != null && response.getResult().getResultCode() != ACCEPTED) {
            LOG.error("Failed to send the roaming authorisation list: {}", response.getResult().getResultDescription());
        }
    }

    @Override
    public List<Transaction> getTransactionList() {
        LOG.info("Retrieving the OCHP transaction information");
        Echs ochpClientService = this.createOchpClientService();
        GetCDRsResponse response = ochpClientService.getCDRs(new GetCDRsRequest(), authenticate());

        List<Transaction> transactions = Lists.newArrayList();
        if (response.getResult() != null && response.getResult().getResultCode() == ACCEPTED) {

            for (CDRInfo cdr : response.getCdrInfoArray()) {
                Transaction transaction = new Transaction(cdr.getCdrId());
                transaction.setIdentificationId(cdr.getAuthenticationId());
                transaction.setTransactionId(cdr.getCdrId());
                transaction.setEvseId(cdr.getEvseId());
                if(cdr.getStartDatetime() != null) {
                    transaction.setTimeStart(DateFormatter.fromISO8601(cdr.getStartDatetime()));
                }
                if(cdr.getEndDatetime() != null) {
                    transaction.setTimeStop(DateFormatter.fromISO8601(cdr.getEndDatetime()));
                }
                transaction.setChargingStation(chargingStationRepository.findByChargingStationId(cdr.getChargePointId()));
                //transaction setMeterStart(0) //TODO: No meterstart is present, only volume, convert our meterstart and meterstop to a volume - Ingo Pak, 14 Mar 2014
                //transaction setMeterStop(Integer parseInt(cdr getVolume()))  //TODO: Converting a string that holds a float into an integer is no good, see previous todo - Ingo Pak, 14 Mar 2014

                transactions.add(transaction);
            }
        }

        return transactions;
    }

    @Override
    public List<ChargingStation> getChargePointList() {
        LOG.info("Retrieving the OCHP chargePoint information");
        Echs ochpClientService = this.createOchpClientService();
        GetChargepointListResponse response = ochpClientService.getChargepointList(new GetChargepointListRequest(), authenticate());

        List<ChargingStation> chargingStations = Lists.newArrayList();
        if (response.getResult() != null && response.getResult().getResultCode() == ACCEPTED) {

            for (ChargepointInfo chargepointInfo : response.getChargepointInfoArray()){
                ChargingStation chargingStation = new ChargingStation(chargepointInfo.getEvseId());
                //TODO: might have to convert more fields - Ingo Pak, 14 Mar 2014
                chargingStations.add(chargingStation);
            }
        }

        return chargingStations;
    }

    @Override
    public List<Identification> getRoamingAuthorizationList() {
        LOG.info("Retrieving the OCHP roaming authorization list");
        Echs ochpClientService = this.createOchpClientService();
        GetRoamingAuthorisationListResponse response = ochpClientService.getRoamingAuthorisationList(new GetRoamingAuthorisationListRequest(), authenticate());

        List<Identification> identifications = Lists.newArrayList();
        if(response.getResult() != null && response.getResult().getResultCode() == ACCEPTED) {
            for (RoamingAuthorisationInfo authorisationInfo : response.getRoamingAuthorisationInfoArray()) {
                AuthorizationResultStatus status = (authorisationInfo.getTokenActivated() == ACTIVATED)?AuthorizationResultStatus.ACCEPTED: AuthorizationResultStatus.INVALID;
                identifications.add(new Identification(authorisationInfo.getTokenId(), status));
            }
        }

        return identifications;
    }

    /**
     * Sends the complete list of charging stations
     * @param chargingStations the list of charging stations
     */
    @Override
    public void sendChargePointList(List<ChargingStation> chargingStations) {
        LOG.info("Send charge point list");
        SetChargepointListRequest request = new SetChargepointListRequest();
        List<ChargepointInfo> chargepointInfoList = request.getChargepointInfoArray();

        for (ChargingStation chargingStation : chargingStations) {
            ChargepointInfo chargepointInfo = new ChargepointInfo();
            chargepointInfo.setEvseId(chargingStation.getChargingStationId());
            chargepointInfo.setStreetName(chargingStation.getAddress());
            chargepointInfo.setCity(chargingStation.getCity());
            chargepointInfo.setPostalCode(chargingStation.getPostalCode());
            chargepointInfo.setTaLat(Double.toString(chargingStation.getLatitude()));
            chargepointInfo.setTaLon(Double.toString(chargingStation.getLongitude()));

            //TODO: add more fields to chargepointInfo - Mark Manders 2014-03-14

            chargepointInfoList.add(chargepointInfo);
        }

        SetChargepointListResponse response = createOchpClientService().setChargepointList(request, authenticate());

        if (response.getResult() != null && response.getResult().getResultCode() != ACCEPTED) {
            LOG.error("Failed to send the charge point list: {}", response.getResult().getResultDescription());
        }
    }

    public void setOchpProxyFactory(OchpProxyFactory ochpProxyFactory) {
        this.ochpProxyFactory = ochpProxyFactory;
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    private Echs createOchpClientService() {
        return ochpProxyFactory.createOchpService(this.serverAddress);
    }
}
