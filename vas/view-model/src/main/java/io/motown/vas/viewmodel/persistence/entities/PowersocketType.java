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

import io.motown.vas.viewmodel.CdrChargePointType;
import io.motown.vas.viewmodel.VasChargingCapability;
import io.motown.vas.viewmodel.VasConnectorType;

import javax.persistence.*;

@Entity
public class PowersocketType{

    @Id
    private String id;

    private Integer essentId;

    private VasConnectorType vasConnectorType;
    private VasChargingCapability vasChargingCapability;
    private CdrChargePointType cdrChargePointType;

    @OneToOne (fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Powersocket powersocket;

    @Column(nullable = false)
    private String name;
    private String logoUrl;
    private Integer socketKwh;

    private PowersocketType(){
        //Private no-arg constructor for Hibernate
    }

    public void setEssentId(Integer essentId) {
        this.essentId = essentId;
    }

    public void setVasConnectorType(VasConnectorType vasConnectorType) {
        this.vasConnectorType = vasConnectorType;
    }

    public void setVasChargingCapability(VasChargingCapability vasChargingCapability) {
        this.vasChargingCapability = vasChargingCapability;
    }

    public void setCdrChargePointType(CdrChargePointType cdrChargePointType) {
        this.cdrChargePointType = cdrChargePointType;
    }

    public void setPowersocket(Powersocket powersocket) {
        this.powersocket = powersocket;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setSocketKwh(Integer socketKwh) {
        this.socketKwh = socketKwh;
    }

    public String getId() {
        return id;
    }

    public Integer getEssentId() {
        return essentId;
    }

    public VasConnectorType getVasConnectorType() {
        return vasConnectorType;
    }

    public VasChargingCapability getVasChargingCapability() {
        return vasChargingCapability;
    }

    public CdrChargePointType getCdrChargePointType() {
        return cdrChargePointType;
    }

    public Powersocket getPowersocket() {
        return powersocket;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Integer getSocketKwh() {
        return socketKwh;
    }
}
