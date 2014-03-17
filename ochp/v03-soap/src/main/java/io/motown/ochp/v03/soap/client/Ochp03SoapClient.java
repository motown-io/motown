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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class Ochp03SoapClient implements Ochp03Client {

    private static final Logger LOG = LoggerFactory.getLogger(Ochp03SoapClient.class);

    private static final int ACCEPTED = 0;

    private static final int DEACTIVATED = 0;
    private static final int ACTIVATED = 1;

    private OchpProxyFactory ochpProxyFactory;

    private ChargingStationRepository chargingStationRepository;

    @Value("${io.motown.ochp.server.address}")
    private String serverAddress;

    @Value("${io.motown.ochp.server.address}")
    private String username;

    @Value("${io.motown.ochp.server.address}")
    private String password;

    /** In memory cache to store the authentication token that is required in all webservice calls */
    private String cachedAuthenticationToken;

    /**
     * Performs authentication in case we do not have an authentication token
     */
    private void forceAuthentication() {
        if(cachedAuthenticationToken == null){
            LOG.info("Not authenticated yet, performing OCHP authenticate");

            Echs ochpClientService = this.createOchpClientService();

            AuthenticateRequest request = new AuthenticateRequest();
            request.setUserId(username);
            request.setPassword(password);
            AuthenticateResponse response = ochpClientService.authenticate(new AuthenticateRequest());

            if(response.getResultCode() != ACCEPTED) {
                LOG.error("Authentication of {} failed", username);
            } else {
                LOG.info("Authentication was successfull");
                cachedAuthenticationToken = response.getAuthToken();
            }
        }
    }

    @Override
    public void addChargeDetailRecords(List<Transaction> transactionList) {
        LOG.info("Add {} CDRs");
        forceAuthentication();

        AddCDRsRequest request = new AddCDRsRequest();
        List<CDRInfo> cdrInfoList = request.getCdrInfoArray();
        for(Transaction transaction : transactionList){
            ChargingStation chargingStation = transaction.getChargingStation();

            CDRInfo cdrInfo = new CDRInfo();
            cdrInfo.setAuthenticationId(transaction.getIdentificationId());
            cdrInfo.setCdrId(transaction.getTransactionId());
            cdrInfo.setEvseId(transaction.getEvseId());
            cdrInfo.setDuration(DateFormatter.formatDuration(transaction.getTimeStart(), transaction.getTimeStop()));
            cdrInfo.setStartDatetime(DateFormatter.toISO8601(transaction.getTimeStart()));
            cdrInfo.setEndDatetime(DateFormatter.toISO8601(transaction.getTimeStop()));
            cdrInfo.setChargePointId(chargingStation.getChargingStationId());
            cdrInfo.setVolume(String.format("%.4f", transaction.calculateVolume()));
/* TODO: Decide if the fields below will be provided - Ingo Pak, 11 Mar 2014
            cdrInfo.setChargePointAddress();
            cdrInfo.setChargePointCity();
            cdrInfo.setChargePointZip();
            cdrInfo.setChargePointCountry();
            cdrInfo.setChargePointType();
            cdrInfo.setInfraProviderId();
            cdrInfo.setMeterId(); //identification of the physical energy meter
            cdrInfo.setObisCode(); //object identification of the register in the meter (IEC 62056-61 eg. 1-1:1.8.0)
            cdrInfo.setProductType(); //identifies the type of product that is delivered
            cdrInfo.setTariffType();
            cdrInfo.setServiceProviderId();
            cdrInfo.setEvcoId(); //identifies a customer in the electric mobility charging context (http://data.fir.de/projektseiten/emobility-ids/)
*/
            cdrInfoList.add(cdrInfo);
        }

        AddCDRsResponse response = this.createOchpClientService().addCDRs(request, cachedAuthenticationToken);

        if(response.getResult() == null || response.getResult().getResultCode() != ACCEPTED) {
            LOG.error("Failed to add the CDR's: {}", response.getResult().getResultDescription());
        }
    }

    /**
     * Sends the complete list of known identification tokens
     * @param identifications
     */
    @Override
    public void sendAuthorizationInformation(List<Identification> identifications) {
        LOG.info("Send authorisation list");
        forceAuthentication();

        SetRoamingAuthorisationListRequest request = new SetRoamingAuthorisationListRequest();
        List<RoamingAuthorisationInfo> roamingAuthorisationList = request.getRoamingAuthorisationInfoArray();

        for (Identification identification : identifications){
            RoamingAuthorisationInfo authorisationInfo = new RoamingAuthorisationInfo();
            //TODO: Spec is unclear if tokenId or evcoId(emtId) should be used, and which information is optional - Ingo Pak, 13 Mar 2014
            authorisationInfo.setEvcoId(identification.getIdentificationId());
            authorisationInfo.setTokenId(identification.getIdentificationId());
            if(AuthorizationResultStatus.ACCEPTED.equals(identification.getAuthorizationStatus())) {
                authorisationInfo.setTokenActivated(ACTIVATED);
            } else {
                authorisationInfo.setTokenActivated(DEACTIVATED);
            }

            roamingAuthorisationList.add(authorisationInfo);
        }

        SetRoamingAuthorisationListResponse response = this.createOchpClientService().setRoamingAuthorisationList(request, cachedAuthenticationToken);

        if(response.getResult() != null && response.getResult().getResultCode() != ACCEPTED) {
            LOG.error("Failed to send the roaming authorisation list: {}", response.getResult().getResultDescription());
        }
    }

    @Override
    public List<Transaction> getTransactionList() {
        LOG.info("Retrieving the OCHP transaction information");
        forceAuthentication();

        Echs ochpClientService = this.createOchpClientService();
        GetCDRsResponse response = ochpClientService.getCDRs(new GetCDRsRequest(), cachedAuthenticationToken);

        List<Transaction> transactions = Lists.newArrayList();
        if (response.getResult() != null && response.getResult().getResultCode() == ACCEPTED) {

            for (CDRInfo cdr : response.getCdrInfoArray()) {
                Transaction transaction = new Transaction(cdr.getCdrId());
                transaction.setIdentificationId(cdr.getAuthenticationId());
                transaction.setTransactionId(cdr.getCdrId());
                transaction.setEvseId(cdr.getEvseId());
                //TODO: Parse date ISO8601 string to date - Ingo Pak, 14 Mar 2014 -> transaction.setTimeStart(cdr.getStartDatetime());
                //TODO: Parse date ISO8601 string to date - Ingo Pak, 14 Mar 2014 -> transaction.setTimeStop(cdr.getEndDatetime());
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
        forceAuthentication();

        Echs ochpClientService = this.createOchpClientService();
        GetChargepointListResponse response = ochpClientService.getChargepointList(new GetChargepointListRequest(), cachedAuthenticationToken);

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
        forceAuthentication();

        Echs ochpClientService = this.createOchpClientService();
        GetRoamingAuthorisationListResponse response = ochpClientService.getRoamingAuthorisationList(new GetRoamingAuthorisationListRequest(), cachedAuthenticationToken);

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
        forceAuthentication();

        SetChargepointListRequest request = new SetChargepointListRequest();
        List<ChargepointInfo> chargepointInfoList = request.getChargepointInfoArray();

        for (ChargingStation chargingStation : chargingStations) {
            ChargepointInfo chargepointInfo = new ChargepointInfo();
            chargepointInfo.setEvseId(chargingStation.getChargingStationId());
            //TODO: add more fields to chargepointInfo - Mark Manders 2014-03-14

            chargepointInfoList.add(chargepointInfo);
        }

        SetChargepointListResponse response = createOchpClientService().setChargepointList(request, cachedAuthenticationToken);

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

    private Echs createOchpClientService() {
        return ochpProxyFactory.createOchpService(this.serverAddress);
    }
}
