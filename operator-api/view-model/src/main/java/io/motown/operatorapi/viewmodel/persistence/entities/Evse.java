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

import io.motown.domain.api.chargingstation.ComponentStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Evse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String evseId;
    private ComponentStatus status;
    @ElementCollection
    private List<Connector> connectors = new ArrayList<>();
    private Availability availability;

    private Evse() {
    }

    public Evse(String evseId) {
        this.evseId = evseId;
    }

    public Evse(String evseId, ComponentStatus status) {
        this.evseId = evseId;
        this.status = status;
    }

    public String getEvseId() {
        return evseId;
    }

    public void setEvseId(String evseId) {
        this.evseId = evseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComponentStatus getStatus() {
        return status;
    }

    public void setStatus(ComponentStatus status) {
        this.status = status;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Availability getAvailability() {
        return availability;
    }
}
