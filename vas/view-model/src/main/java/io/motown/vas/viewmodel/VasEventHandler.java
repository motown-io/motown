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
package io.motown.vas.viewmodel;

import io.motown.domain.api.chargingstation.*;
import io.motown.vas.viewmodel.model.ChargingStation;
import io.motown.vas.viewmodel.persistence.repostories.ChargingStationRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VasEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(io.motown.vas.viewmodel.VasEventHandler.class);

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    //TODO: Determine which event handlers are necessary to keep the model state up to date - Ingo Pak, 22 Jan 2014

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        LOG.info("Handling ChargingStationCreatedEvent");

        String chargingStationId = event.getChargingStationId().getId();
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId);

        if (chargingStation == null) {
            chargingStation = new ChargingStation(chargingStationId);
        }

        chargingStationRepository.save(chargingStation);
    }

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        LOG.debug("ChargingStationAcceptedEvent for {} received!", event.getChargingStationId());

        ChargingStation chargingStation = getChargingStation(event.getChargingStationId());

        if (chargingStation != null) {
            chargingStation.setRegistered(true);
            chargingStationRepository.save(chargingStation);
        }
    }

    @EventHandler
    public void handle(ChargingStationConfiguredEvent event) {
        LOG.info("ChargingStationConfiguredEvent");

        String chargingStationId = event.getChargingStationId().getId();
        ChargingStation chargingStation = getChargingStation(event.getChargingStationId());

        if (chargingStation == null) {
            LOG.warn("Received a ChargingStationConfiguredEvent for unknown charging station. Creating the chargingStation.");
            chargingStation = new ChargingStation(chargingStationId);
        }

        //TODO determine charge mode based on the info in event.getEvses() - Mark van den Bergh, Februari 13th 2014
        //chargingStation.setChargeMode(ChargeMode.MODE3);

        //TODO determine list of connector types based on the info in event.getEvses() - Mark van den Bergh, Februari 13th 2014
        //chargingStation.setConnectorTypes();

        //TODO determine the info for the Evse list - Mark van den Bergh, Februari 13th 2014
        //chargingStation.setEvses();

        chargingStation.setConfigured(true);

        /*
            // this should probably move to some kind of matcher class, commecnted here to not loose the info:

            // VAS connector types matched to OCPP 2.0 connector type:

            // UNSPECIFIED
            Unspecified,

            // W_INDUCTIVE
            Small_Paddle_Inductive,
            // W_INDUCTIVE
            Large_Paddle_Inductive,

            Avcon_Connector,

            // TESLA
            Tesla_Connector,

            Sae_J1772_Yazaki,

            Nema_520,

            // C_G105
            Tepco_Cha_Me_Do,

            // C_TYPE_1 (captive enabled)
            Iec_621962_Type_1_Yazaki,

            // C_TYPE_2 (captive enabled), S_TYPE_2
            Iec_621962_Type_2_Mennekes,

            // S_TYPE_3
            Iec_621962_Type_3_Scame,

            _60309Industrial2PDc,

            // S_309_1P_16A or S_309_1P_32A ?
            _60309IndustrialPneAc,

            // S_309_3P_16A or S_309_3P_32A ?
            _60309Industrial3PEAc,

            // S_309_3P_16A or S_309_3P_32A ?
            _60309Industrial3PENAc,

            DomesticPlugTypeANema115Unpolarised,
            DomesticPlugTypeANema115Polarised,
            DomesticPlugTypeAJisC8303ClassII,
            DomesticPlugTypeBNema515,
            DomesticPlugTypeBNema520,
            DomesticPlugTypeBJis8393ClassI,
            DomesticPlugTypeCCee716Europlug,
            DomesticPlugTypeCCee717,
            DomesticPlugTypeCGost7396C1,
            DomesticPlugTypeDBs5462Pin,
            DomesticPlugTypeDBs5463Pin,

            // CEE_7_7
            DomesticPlugTypeECee75,

            // CEE_7_7
            DomesticPlugTypeFCee74Schuko,

            // CEE_7_7
            DomesticPlugTypeEFCee77,

            DomesticPlugTypeGBs1363Is401411Ms58,
            DomesticPlugTypeHSi32,
            DomesticPlugTypeIAsNzs3112,
            DomesticPlugTypeICpcsCcc,
            DomesticPlugTypeIIram2073,
            DomesticPlugTypeJSev1011,
            DomesticPlugTypeKSection1072D1,
            DomesticPlugTypeKThailandTis1662549,
            DomesticPlugTypeLCei2316VII,
            DomesticPlugTypeIec6090612Pin,
            DomesticPlugTypeIec6090613Pin
        */

        chargingStationRepository.save(chargingStation);
    }

    @EventHandler
    public void handle(ChargingStationPlacedEvent event) {
        LOG.info("ChargingStationPlacedEvent");

        updateLocationForChargingStation(event.getChargingStationId(), event.getCoordinates(), event.getAddress());
    }

    @EventHandler
    public void handle(ChargingStationLocationImprovedEvent event) {
        LOG.info("ChargingStationLocationImprovedEvent");

        updateLocationForChargingStation(event.getChargingStationId(), event.getCoordinates(), event.getAddress());
    }

    @EventHandler
    public void handle(ChargingStationMovedEvent event) {
        LOG.info("ChargingStationMovedEvent");

        updateLocationForChargingStation(event.getChargingStationId(), event.getCoordinates(), event.getAddress());
    }

    @EventHandler
    public void handle(ChargingStationMadeReservableEvent event) {
        LOG.info("ChargingStationMadeReservableEvent for {} received", event.getChargingStationId());

        updateReservableForChargingStation(event.getChargingStationId(), true);
    }

    @EventHandler
    public void handle(ChargingStationMadeNotReservableEvent event) {
        LOG.info("ChargingStationMadeNotReservableEvent for {} received", event.getChargingStationId());

        updateReservableForChargingStation(event.getChargingStationId(), false);
    }

    @EventHandler
    public void handle(ChargingStationStatusNotificationReceivedEvent event) {
        //TODO implement. - Mark van den Bergh, Februari 13th 2014
    }

    @EventHandler
    public void handle(ComponentStatusNotificationReceivedEvent event) {
        //TODO implement. - Mark van den Bergh, Februari 13th 2014
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }

    /**
     * Tries to find a {@code ChargingStation} by the passed id, if not found an error will be logged and null will be
     * returned.
     *
     * @param chargingStationId    charging station identifier.
     * @return charging station if found, null otherwise.
     */
    private ChargingStation getChargingStation(ChargingStationId chargingStationId) {
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());

        if (chargingStation == null) {
            LOG.error("Could not find charging station {}", chargingStationId);
        }

        return chargingStation;
    }

    /**
     * Updates the 'reservable' property of the charging station. If the charging station cannot be found in the
     * repository an error is logged.
     *
     * @param chargingStationId charging station identifier.
     * @param reservable true if the charging station is reservable, false otherwise.
     */
    private void updateReservableForChargingStation(ChargingStationId chargingStationId, boolean reservable) {
        ChargingStation chargingStation = getChargingStation(chargingStationId);

        if (chargingStation != null) {
            chargingStation.setReservable(reservable);
            chargingStationRepository.save(chargingStation);
        }
    }

    /**
     * Updates the location of the charging station to either new lat/long coordinates or a geographical address (or both).
     * If the charging station cannot be found in the repository an error is logged.
     *
     * @param chargingStationId charging station identifier.
     * @param coordinates the lat/long coordinates of the charging station.
     * @param address the geographical address of the charging station.
     */
    private void updateLocationForChargingStation(ChargingStationId chargingStationId, Coordinates coordinates, Address address) {
        ChargingStation chargingStation = getChargingStation(chargingStationId);

        if (chargingStation != null) {

            if (coordinates != null) {
                chargingStation.setLatitude(coordinates.getLatitude());
                chargingStation.setLongitude(coordinates.getLongitude());
            }

            if (address != null) {
                chargingStation.setAddress(address.getAddressline1());
                chargingStation.setCity(address.getCity());
                chargingStation.setCountry(address.getCountry());
                chargingStation.setPostalCode(address.getPostalCode());
                chargingStation.setRegion(address.getRegion());
            }

            chargingStationRepository.save(chargingStation);
        }
    }
}
