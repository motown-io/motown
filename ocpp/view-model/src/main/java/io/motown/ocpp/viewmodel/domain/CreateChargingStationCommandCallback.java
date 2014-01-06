package io.motown.ocpp.viewmodel.domain;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import org.axonframework.commandhandling.CommandCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateChargingStationCommandCallback implements CommandCallback<Object> {

    private static final Logger log = LoggerFactory.getLogger(CreateChargingStationCommandCallback.class);

    private ChargingStationId chargingStationId;
    private String chargingStationAddress;
    private String vendor;
    private String model;
    private String protocol;
    private ChargingStationRepository chargingStationRepository;
    private DomainService domainService;

    public CreateChargingStationCommandCallback(ChargingStationId chargingStationId, String chargingStationAddress, String vendor, String model, String protocol, ChargingStationRepository chargingStationRepository, DomainService domainService) {
        this.chargingStationId = chargingStationId;
        this.chargingStationAddress = chargingStationAddress;
        this.vendor = vendor;
        this.model = model;
        this.protocol = protocol;
        this.chargingStationRepository = chargingStationRepository;
        this.domainService = domainService;
    }

    @Override
    public void onSuccess(Object o) {
        chargingStationRepository.save(new ChargingStation(chargingStationId.getId()));

        domainService.bootChargingStation(chargingStationId, chargingStationAddress, vendor, model, protocol);
    }

    @Override
    public void onFailure(Throwable throwable) {
        //TODO what do we do now? Do we still send out a BootChargingStationCommand so other components can react on it? - Mark van den Bergh, December 11th 2013
        log.error("CreateChargingStationCommand failed. " + throwable.getMessage());
    }
}
