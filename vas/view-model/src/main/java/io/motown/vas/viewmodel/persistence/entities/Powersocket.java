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
package io.motown.vas.viewmodel.persistence.entities;

import io.motown.vas.viewmodel.ChargeMode;
import io.motown.vas.viewmodel.State;

import javax.persistence.*;


@Entity
public class Powersocket {

    @Id
    private String id;

    @Column(nullable = false)
    private Integer position;            // indication of the logical position of the socket in a chargepoint

    @Column(nullable = false)
    private State state;                 // the current state of the socket

    @Column(nullable = false)
    private Boolean enabled = false;     // indication if the powersocket is enabled or disabled

    private ChargeMode chargeMode;             // Charge mode for this socket

    private Integer currentMeterValue;

    private Integer socketKwh;

    @OneToOne(mappedBy = "powersocket")
    private PowersocketType powersocketType;

    @ManyToOne
    @JoinColumn(name="id", insertable = false, updatable = false)
    private ChargingStation chargingStation;

    private Powersocket() {
        // Private no-arg constructor for Hibernate.
    }

    public Powersocket(String id, Integer position, State state, Boolean enabled, ChargeMode chargeMode, Integer currentMeterValue, Integer socketKwh) {
        this.id = id;
        this.position = position;
        this.state = state;
        this.enabled = enabled;
        this.chargeMode = chargeMode;
        this.currentMeterValue = currentMeterValue;
        this.socketKwh = socketKwh;
    }

    public String getId() {
        return id;
    }

    public Integer getPosition() {
        return position;
    }

    public State getState() {
        return state;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public ChargeMode getChargeMode() {
        return chargeMode;
    }

    public Integer getCurrentMeterValue() {
        return currentMeterValue;
    }

    public Integer getSocketKwh() {
        return socketKwh;
    }

    public PowersocketType getPowersocketType() {
        return powersocketType;
    }

    public ChargingStation getChargingStation() {
        return chargingStation;
    }
}