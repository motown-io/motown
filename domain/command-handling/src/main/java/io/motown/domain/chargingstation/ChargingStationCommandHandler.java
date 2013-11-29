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
package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChargingStationCommandHandler {

    private Repository<ChargingStation> repository;

    @CommandHandler
    public ChargingStationRegistrationStatus handle(BootChargingStationCommand command) {
        ChargingStation chargingStation = loadOrCreateChargingStation(command.getChargingStationId(), false);
        return chargingStation.handle(command);
    }

    @CommandHandler
    public void handle(RegisterChargingStationCommand command) {
        ChargingStation chargingStation = loadOrCreateChargingStation(command.getChargingStationId(), true);
        chargingStation.handle(command);
    }

    private ChargingStation loadOrCreateChargingStation(ChargingStationId chargingStationId, boolean acceptToNetwork) {
        ChargingStation chargingStation;

        try {
            chargingStation = repository.load(chargingStationId);
        } catch (AggregateNotFoundException e) {
            chargingStation = new ChargingStation(new CreateChargingStationCommand(chargingStationId, acceptToNetwork));
            repository.add(chargingStation);
        }

        return chargingStation;
    }

    @Autowired
    @Qualifier("chargingStationRepository")
    public void setRepository(Repository<ChargingStation> chargingStationRepository) {
        this.repository = chargingStationRepository;
    }
}
