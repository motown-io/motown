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

import static com.google.common.base.Preconditions.checkNotNull;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connector connector = (Connector) o;

        if (maxAmp != connector.maxAmp) return false;
        if (phase != connector.phase) return false;
        if (voltage != connector.voltage) return false;
        if (chargingProtocol != connector.chargingProtocol) return false;
        if (connectorType != connector.connectorType) return false;
        if (current != connector.current) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = maxAmp;
        result = 31 * result + phase;
        result = 31 * result + voltage;
        result = 31 * result + chargingProtocol.hashCode();
        result = 31 * result + current.hashCode();
        result = 31 * result + connectorType.hashCode();
        return result;
    }
}
