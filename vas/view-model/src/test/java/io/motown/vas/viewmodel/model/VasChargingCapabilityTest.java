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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.PHASE;
import static org.junit.Assert.assertEquals;

public class VasChargingCapabilityTest {

    public static final int PHASE_1 = 1;
    public static final int PHASE_UNKNOWN = 9;
    public static final int VOLTAGE_120 = 120;
    public static final int VOLTAGE_240 = 240;
    public static final int VOLTAGE_480 = 480;
    public static final int VOLTAGE_UNKNOWN = 999;
    public static final int MAX_AMP_10 = 10;
    public static final int MAX_AMP_12 = 12;
    public static final int MAX_AMP_16 = 16;
    public static final int MAX_AMP_32 = 32;
    public static final int MAX_AMP_63 = 63;
    public static final int MAX_AMP_UNKNOWN = 999;

    @Test
    public void fromConnector120Volt1Phase10Amp() {
        Connector connector = new Connector(MAX_AMP_10, PHASE_1, VOLTAGE_120, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_120V_1_PHASE_10A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector120Volt1Phase12Amp() {
        Connector connector = new Connector(MAX_AMP_12, PHASE_1, VOLTAGE_120, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_120V_1_PHASE_12A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector120Volt1Phase16Amp() {
        Connector connector = new Connector(MAX_AMP_16, PHASE_1, VOLTAGE_120, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_120V_1_PHASE_16A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt1Phase10Amp() {
        Connector connector = new Connector(MAX_AMP_10, PHASE_1, VOLTAGE_240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_1_PHASE_10A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt1Phase12Amp() {
        Connector connector = new Connector(MAX_AMP_12, PHASE_1, VOLTAGE_240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_1_PHASE_12A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt1Phase16Amp() {
        Connector connector = new Connector(MAX_AMP_16, PHASE_1, VOLTAGE_240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_1_PHASE_16A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt1Phase32Amp() {
        Connector connector = new Connector(MAX_AMP_32, PHASE_1, VOLTAGE_240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_1_PHASE_32A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt3Phase16Amp() {
        Connector connector = new Connector(MAX_AMP_16, PHASE, VOLTAGE_240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_3_PHASE_16A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector240Volt3Phase32Amp() {
        Connector connector = new Connector(MAX_AMP_32, PHASE, VOLTAGE_240, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_240V_3_PHASE_32A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector480Volt3Phase16Amp() {
        Connector connector = new Connector(MAX_AMP_16, PHASE, VOLTAGE_480, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_480V_3_PHASE_16A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector480Volt3Phase32Amp() {
        Connector connector = new Connector(MAX_AMP_32, PHASE, VOLTAGE_480, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_480V_3_PHASE_32A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnector480Volt3Phase63Amp() {
        Connector connector = new Connector(MAX_AMP_63, PHASE, VOLTAGE_480, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.V_480V_3_PHASE_63A, VasChargingCapability.fromConnector(connector));
    }

    @Test
    public void fromConnectorUnknownToUnspecified() {
        Connector connector = new Connector(MAX_AMP_UNKNOWN, PHASE_UNKNOWN, VOLTAGE_UNKNOWN, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1);

        assertEquals(VasChargingCapability.UNSPECIFIED, VasChargingCapability.fromConnector(connector));
    }
}
