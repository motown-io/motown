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
package io.motown.ocpp.viewmodel;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.common.annotation.MetaData;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class OcppRequestEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OcppRequestEventHandler.class);

    /**
     * OcppRequest handlers based on protocol.
     */
    private Map<String, OcppRequestHandler> requestHandlers = new HashMap<>();

    private ChargingStationRepository chargingStationRepository;

    @EventHandler
    public void handle(ConfigurationItemsRequestedEvent event) {
        LOG.info("Handling ConfigurationItemsRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event);
        }
    }

    @EventHandler
    public void handle(StopTransactionRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("StopTransactionRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(SoftResetChargingStationRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("SoftResetChargingStationRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(HardResetChargingStationRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("HardResetChargingStationRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(StartTransactionRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("StartTransactionRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(UnlockEvseRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("UnlockEvseRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeChargingStationAvailabilityToInoperativeRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeChargingStationAvailabilityToOperativeRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(DataTransferEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("DataTransferEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(ChangeConfigurationItemEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeConfigurationItemEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(DiagnosticsRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("DiagnosticsRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(ClearCacheRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("ClearCacheRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("FirmwareUpdateRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event);
        }
    }

    @EventHandler
    public void handle(AuthorizationListVersionRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("AuthorizationListVersionRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(SendAuthorizationListRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("SendAuthorizationListRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    @EventHandler
    public void handle(ReserveNowRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("ReserveNowRequestedEvent");

        OcppRequestHandler ocppRequestHandler = getOcppRequestHandler(event.getChargingStationId());

        if (ocppRequestHandler != null) {
            ocppRequestHandler.handle(event, statusCorrelationToken);
        }
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }

    public void setRequestHandlers(Map<String, OcppRequestHandler> requestHandlers) {
        this.requestHandlers = requestHandlers;
    }

    private OcppRequestHandler getOcppRequestHandler(ChargingStationId chargingStationId) {
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());

        if (chargingStation != null && chargingStation.getProtocol() != null) {
            return requestHandlers.get(chargingStation.getProtocol());
        } else {
            return null;
        }
    }
}
