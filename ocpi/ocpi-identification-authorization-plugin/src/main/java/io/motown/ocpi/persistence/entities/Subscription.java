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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.motown.ocpi.persistence.entities.Endpoint.ModuleIdentifier;

/**
 * Subscription entity class
 * 
 * @author bartwolfs
 *
 */
@Entity
@Table(name = "SUBSCRIPTION")
public class Subscription {

	private static final Logger LOG = LoggerFactory.getLogger(Subscription.class);

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "SUBSCRIPTION_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "SUBSCRIPTION_SEQ")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "business_details_id")
	private BusinessDetails businessDetails;

	@Column(name = "OCPI_VERSION")
	private String ocpiVersion; // OCPI version to use

	@Column(name = "LUKAS_AUTHORIZATION_TOKEN")
	private String lukasAuthorizationToken; // token to be used when contacting
											// Lukas

	@Column(name = "PARTNER_AUTHORIZATION_TOKEN")
	private String partnerAuthorizationToken; // token to be used when
												// contacting the partners OCPI
												// endpoints

	@OneToMany(cascade = CascadeType.ALL, targetEntity = Endpoint.class, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "subscription")
	private Set<Endpoint> endpoints = new HashSet<>();

	public Subscription() {

	}

	public void addToEndpoints(Endpoint endpoint) {
		endpoint.setSubscription(this);
		this.endpoints.add(endpoint);
	}

	public void generateNewLukasAuthorizationToken() {
		lukasAuthorizationToken = UUID.randomUUID().toString();
	}

	public BusinessDetails getBusinessDetails() {
		return businessDetails;
	}

	/**
	 * returns the Endpoint with the identifier passed as argument if not found
	 * returns null
	 *
	 * @param identifier
	 * @return Endpoint
	 */
	public Endpoint getEndpoint(ModuleIdentifier identifier) {
		for (Endpoint endpoint : getEndpoints()) {
			if (endpoint.getIdentifier().equals(identifier)) {
				return endpoint;
			}
		}
		return null;
	}

	public Collection<Endpoint> getEndpoints() {
		return endpoints;
	}

	public Integer getId() {
		return id;
	}

	public String getLukasAuthorizationToken() {
		return lukasAuthorizationToken;
	}

	public String getOcpiVersion() {
		return ocpiVersion;
	}

	public String getPartnerAuthorizationToken() {
		return partnerAuthorizationToken;
	}

	/**
	 * returns true if this subscription is not yet registered with the emsp
	 * 
	 * @return boolean
	 */
	public boolean isRegistered() {
		return getEndpoint(ModuleIdentifier.VERSIONS) == null;
	}

	public void setBusinessDetails(BusinessDetails businessDetails) {
		this.businessDetails = businessDetails;
	}

	public void setEndpoints(Set<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public void setLukasAuthorizationToken(String lukasAuthorizationToken) {
		this.lukasAuthorizationToken = lukasAuthorizationToken;
	}

	public void setOcpiVersion(String ocpiVersion) {
		this.ocpiVersion = ocpiVersion;
	}

	public void setPartnerAuthorizationToken(String partnerAuthorizationToken) {
		this.partnerAuthorizationToken = partnerAuthorizationToken;
	}
}
