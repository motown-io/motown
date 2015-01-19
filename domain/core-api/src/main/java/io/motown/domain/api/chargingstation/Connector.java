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
package io.motown.domain.api.chargingstation;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Connector is an independently operated and managed electrical outlet on an EVSE. This corresponds to a single
 * physical outlet.
 */
public final class Connector {

    private int maxAmp;

    private int phase;

    private int voltage;

    private ChargingProtocol chargingProtocol;

    private Current current;

    private ConnectorType connectorType;

    public Connector(int maxAmp, int phase, int voltage, ChargingProtocol chargingProtocol, Current current, ConnectorType connectorType) {
        this.maxAmp = maxAmp;
        this.phase = phase;
        this.voltage = voltage;
        this.chargingProtocol = checkNotNull(chargingProtocol);
        this.current = checkNotNull(current);
        this.connectorType = checkNotNull(connectorType);
    }

    public int getMaxAmp() {
        return maxAmp;
    }

    public int getPhase() {
        return phase;
    }

    public int getVoltage() {
        return voltage;
    }

    public ChargingProtocol getChargingProtocol() {
        return chargingProtocol;
    }

    public Current getCurrent() {
        return current;
    }

    public ConnectorType getConnectorType() {
        return connectorType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxAmp, phase, voltage, chargingProtocol, current, connectorType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Connector other = (Connector) obj;
        return Objects.equals(this.maxAmp, other.maxAmp) && Objects.equals(this.phase, other.phase) && Objects.equals(this.voltage, other.voltage) && Objects.equals(this.chargingProtocol, other.chargingProtocol) && Objects.equals(this.current, other.current) && Objects.equals(this.connectorType, other.connectorType);
    }
}
