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
package io.motown.ocpi.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * TokenSyncDate entity class
 * 
 * @author bartwolfs
 *
 */
@Entity
@Table(name = "TOKEN_SYNC_DATE")
public class TokenSyncDate {

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "TOKEN_SYNC_DATE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "TOKEN_SYNC_DATE_SEQ")
	private Integer id;

	@Column(name = "SYNC_DATE", nullable = false)
	private Date syncDate;

	@Column(name = "SUBSCRIPTION_ID", nullable = false)
	private Integer subscriptionId;

	
	public TokenSyncDate() {

	}

	public Integer getId() {
		return id;
	}

	public Integer getSubscriptionId() {
		return subscriptionId;
	}

	public Date getSyncDate() {
		return syncDate;
	}
	
	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}
}
