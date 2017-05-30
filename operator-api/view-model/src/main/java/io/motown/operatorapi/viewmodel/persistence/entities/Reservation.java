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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;

import io.motown.domain.api.chargingstation.EvseId;
import io.motown.domain.api.chargingstation.ReservationStatus;

@Entity
public class Reservation {

    @Id
    @SequenceGenerator(name="reservationSeq", allocationSize=1, initialValue=1)
    @GeneratedValue(generator="reservationSeq")
    private Long id;
    
    private Date updated;
    private Date created;

    private String chargingStationId;

    private String userId;
    
    @Column(unique = true)
    private String reservationId;

    @Embedded
    @AttributeOverride( name="id", column=@Column(name = "evseId") )
    private EvseId evseId;

    private Date expiryDate;

    private ReservationStatus status;
    
    private Reservation() {
        // Private no-arguments constructor for Hibernate.
    }

    public Reservation(String chargingStationId, ReservationStatus status, String reservationId, String userId, EvseId evseId, Date expiryDate) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.status = status;
        if (ReservationStatus.ACCEPTED.equals(status))
        {
        	checkNotNull(reservationId);
        	checkNotNull(expiryDate);
        }
        this.reservationId = reservationId;
        this.evseId = checkNotNull(evseId);
        this.userId = checkNotNull(userId);
        this.expiryDate = expiryDate;
    }

    public String getChargingStationId() {
        return chargingStationId;
    }

    public Date getCreated() {
        return created != null ? new Date(created.getTime()) : null;
    }

    public EvseId getEvseId() {
        return evseId;
    }

    public Date getExpiryDate() {
		return expiryDate;
	}

    public Long getId() {
        return id;
    }

    public String getReservationId() {
		return reservationId;
	}

    public ReservationStatus getStatus() {
		return status;
	}

    public Date getUpdated() {
        return updated != null ? new Date(updated.getTime()) : null;
    }
    
    public String getUserId() {
		return userId;
	}

	@PrePersist
    protected void onCreate() {
        Date now = new Date();
        created = now;
        updated = now;
    }

	@PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }

	
	//TODO ZIJN DEZE NODIG?
	public void setChargingStationId(String chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

	public void setEvseId(EvseId evseId) {
        this.evseId = evseId;
    }

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
//NODIG?
	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

}
