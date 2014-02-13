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
package io.motown.vas.viewmodel.model;

import io.motown.domain.api.chargingstation.ChargingProtocol;
import io.motown.domain.api.chargingstation.Connector;
import io.motown.domain.api.chargingstation.ConnectorType;
import io.motown.domain.api.chargingstation.Current;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VasChargingCapabilityTest {

    @Test
    public void fromConnector120Volt1Phase10Amp() {
        Connector connector = new Connector(10, 1, 120, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_120V_1_PHASE_10A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector120Volt1Phase12Amp() {
        Connector connector = new Connector(12, 1, 120, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_120V_1_PHASE_12A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector120Volt1Phase16Amp() {
        Connector connector = new Connector(16, 1, 120, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_120V_1_PHASE_16A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt1Phase10Amp() {
        Connector connector = new Connector(10, 1, 240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_1_PHASE_10A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt1Phase12Amp() {
        Connector connector = new Connector(12, 1, 240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_1_PHASE_12A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt1Phase16Amp() {
        Connector connector = new Connector(16, 1, 240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_1_PHASE_16A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt1Phase32Amp() {
        Connector connector = new Connector(32, 1, 240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_1_PHASE_32A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt3Phase16Amp() {
        Connector connector = new Connector(16, 3, 240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_3_PHASE_16A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt3Phase32Amp() {
        Connector connector = new Connector(32, 3, 240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_3_PHASE_32A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector480Volt3Phase16Amp() {
        Connector connector = new Connector(16, 3, 480, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_480V_3_PHASE_16A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector480Volt3Phase32Amp() {
        Connector connector = new Connector(32, 3, 480, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_480V_3_PHASE_32A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector480Volt3Phase63Amp() {
        Connector connector = new Connector(63, 3, 480, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_480V_3_PHASE_63A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnectorUnknownToUnspecified() {
        Connector connector = new Connector(999, 9, 999, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.UNSPECIFIED, VasChargingCapability.fromConnector(connector));
    }
}
