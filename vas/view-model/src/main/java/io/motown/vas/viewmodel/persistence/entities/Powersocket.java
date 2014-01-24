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
@NamedQueries({
    @NamedQuery(name="enabledPowerSocketsForChargepoint", query="from Powersocket where enabled = true and chargePointId = :chargePointId")
})
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

    /*  //TODO: convert code below - Ingo Pak, 22 Jan 2014

    static namedQueries = {

        authorizedPowersockets { currentOperator ->
            if(!currentOperator.unrestrictedAccess) {
                chargepoint {
                    pools {
                        // Each pool of this chargepoint should match at least one of the current operators' pools in
                        // order to be shown in Lukas
                        or {
                            currentOperator.pools.each { pool ->
                                idEq(pool.id)
                            }
                        }
                    }
                }
            }
        }

        // retrieves all enabled powersockets for a specific chargepoint
        enabledPowerSocketsForChargepoint { chargePointId ->
            chargepoint {
                idEq(chargePointId)
            }
            eq("enabled", true)
        }

        // retrieves a chargepoint on a specific position
        onPosition{ position ->
            eq("position", position)
        }

        powersocketsForPool { Pool pool ->
            chargepoint {
                pools {
                    idEq(pool.id)
                }
            }
        }

        occupied {
            eq("state", State.OCCUPIED)
        }

        available {
            eq("state", State.AVAILABLE)
        }

        filterOnState { State state ->
            if (state) {
                eq 'state', state
            }
        }
    }

    def getDisplayName() {
        "${position}: ${powersocketType.name} [${state}]"
    }

*/
    /**
     * We only allow 1 enabled powersocket per location, per chargepoint! So we check if the powersocket is valid for
     * this position.
     *
     * @param position
     * @param powerSocket
     * @return
     */
/*    def isValid(position, Powersocket powerSocket) {
        // Running this validation in a separate session as not doing this will cause
        // issues. Because we're accessing the database in the validation below it would
        // have caused a flush but because the validation happens before the actual
        // flushing to the database it will cause the error 'collection [...] was not
        // processed by flush' when trying to load the new objects into the session.
        withNewSession {
            // value of enabled should be unique for a specified position and chargepoint
            List powersocketList = Powersocket.enabledPowerSocketsForChargepoint(powerSocket?.chargepoint?.id).onPosition(position).list()

            // if the current powersocket is not enabled or there are no active powersockets we can accept them directly
            if(!powerSocket.enabled || powersocketList?.size() == 0) {
                return true
            }

            // if the powersocket list contains 1 powersocket for this position which is normal,
            // then check if we are validating the one in the list..
            if(powersocketList?.size() == 1) {
                // we are evaluating the same powersocket so its ok..
                if(powersocketList.first().id == powerSocket.id) {
                    return true
                }

                if(powersocketList.first().position == powerSocket.position) {
                    return false
                } else {
                    return true
                }
            }

            // if the powersocket list contains more then 1 powersocket then this is an abnormal situation, so return false
            if(powersocketList?.size() > 1) {
                return false
            }
        }
    }

    def getFavoriteName() {
        return displayName
    }
*/
}