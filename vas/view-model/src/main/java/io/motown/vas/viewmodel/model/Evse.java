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

import javax.persistence.*;


@Entity
public class Evse {

    @Id
    private String id;

    /**
     * Indication of the logical position of the EVSE in a charging station.
     */
    @Column(nullable = false)
    private Integer position;

    /**
     * The current state of the EVSE.
     */
    @Column(nullable = false)
    private State state;

    @ManyToOne
    @JoinColumn(name="id", insertable = false, updatable = false)
    private ChargingStation chargingStation;

    private Evse() {
        // Private no-arg constructor for Hibernate.
    }

    public Evse(String id, Integer position, State state) {
        this.id = id;
        this.position = position;
        this.state = state;
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

    public ChargingStation getChargingStation() {
        return chargingStation;
    }
}
