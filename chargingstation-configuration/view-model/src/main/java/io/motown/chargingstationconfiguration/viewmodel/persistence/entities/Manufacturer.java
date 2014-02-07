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
package io.motown.chargingstationconfiguration.viewmodel.persistence.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Manufacturer {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Code of the manufacturer, used to identify which charging station belongs to a manufacturer.
     */
    private String code;

    /**
     * Charging station types connected to this manufacturer.
     */
    @OneToMany(mappedBy="manufacturer",
               cascade = CascadeType.ALL,
               targetEntity = ChargingStationType.class)
    private List<ChargingStationType> chargingStationTypes;

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ChargingStationType> getChargingStationTypes() {
        return chargingStationTypes;
    }

    public void setChargingStationTypes(List<ChargingStationType> chargingStationTypes) {
        this.chargingStationTypes = chargingStationTypes;
    }

}
