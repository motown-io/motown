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
package io.motown.chargingstationconfiguration.viewmodel.domain;

import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.domain.api.chargingstation.ChargingProtocol;
import io.motown.domain.api.chargingstation.ConnectorType;
import io.motown.domain.api.chargingstation.Current;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestUtils {

    public static Manufacturer getManufacturerWithConfiguration(String vendor, String model) {
        Manufacturer manufacturer = getManufacturer(vendor);

        ChargingStationType type = getChargingStationType(model);

        Evse evseFirst = getEvse(1);
        evseFirst.setConnectors(getConnectors(3));

        Evse evseSecond = getEvse(2);
        evseSecond.setConnectors(getConnectors(3));

        List<Evse> evses = new ArrayList<>(2);
        evses.add(evseFirst);
        evses.add(evseSecond);

        type.setEvses(evses);
        type.setManufacturer(manufacturer);

        List<ChargingStationType> types = new ArrayList<>();
        types.add(type);

        manufacturer.setChargingStationTypes(types);

        return manufacturer;
    }

    public static Manufacturer getManufacturer(String code) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCode(code);
        return manufacturer;
    }

    public static ChargingStationType getChargingStationType(String code) {
        ChargingStationType type = new ChargingStationType();
        type.setCode(code);
        return type;
    }

    public static Evse getEvse(int identifier) {
        Evse evse = new Evse();
        evse.setIdentifier(identifier);
        return evse;
    }

    public static List<Connector> getConnectors(int numberOfConnectors) {
        List<Connector> connectors = new ArrayList<>(numberOfConnectors);
        for (int i = 0; i < numberOfConnectors; i++) {
            connectors.add(getConnector());
        }
        return connectors;
    }

    public static Connector getConnector() {
        Connector connector = new Connector();
        connector.setChargingProtocol(ChargingProtocol.MODE3);
        connector.setConnectorType(ConnectorType.Tesla_Connector);
        connector.setCurrent(Current.AC);
        connector.setMaxAmp(32);
        connector.setPhase(3);
        connector.setVoltage(230);
        return connector;
    }

}
