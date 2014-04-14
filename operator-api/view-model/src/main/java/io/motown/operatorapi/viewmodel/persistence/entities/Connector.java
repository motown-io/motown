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
package io.motown.operatorapi.viewmodel.persistence.entities;

import io.motown.domain.api.chargingstation.ChargingProtocol;
import io.motown.domain.api.chargingstation.ConnectorType;
import io.motown.domain.api.chargingstation.Current;

import javax.persistence.Embeddable;

@Embeddable
public class Connector {
    private int maxAmp;
    private int phase;
    private int voltage;
    private ChargingProtocol chargingProtocol;
    private Current current;
    private ConnectorType type;

    public Connector() {
    }

    public Connector(int maxAmp, int phase, int voltage, ChargingProtocol chargingProtocol, Current current, ConnectorType type) {
        this.maxAmp = maxAmp;
        this.phase = phase;
        this.voltage = voltage;
        this.chargingProtocol = chargingProtocol;
        this.current = current;
        this.type = type;
    }

    public int getMaxAmp() {
        return maxAmp;
    }

    public void setMaxAmp(int maxAmp) {
        this.maxAmp = maxAmp;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public ChargingProtocol getChargingProtocol() {
        return chargingProtocol;
    }

    public void setChargingProtocol(ChargingProtocol chargingProtocol) {
        this.chargingProtocol = chargingProtocol;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public ConnectorType getType() {
        return type;
    }

    public void setType(ConnectorType type) {
        this.type = type;
    }
}
