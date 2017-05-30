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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonCreator;

/**
 * Endpoint entity class
 * 
 * @author bartwolfs
 *
 */
@Entity
@Table(name="ENDPOINT")//, uniqueConstraints = @UniqueConstraint(columnNames = { "code", "manufacturerId" }))
public class Endpoint {

	public enum ModuleIdentifier {
		CDRS("cdrs"), CHARGING_PROFILES("chargingprofiles"), CREDENTIALS("credentials"), LOCATIONS(
				"locations"), SESSIONS("sessions"), TARIFFS("tariffs"), TOKENS("tokens"), VERSIONS("versions");

		@JsonCreator // This is the factory method and must be static
	    public static ModuleIdentifier fromString(String string) {
			for (ModuleIdentifier m : values()) {
				if (m.value.equalsIgnoreCase(string)) {
					return m;
				}
			}
			throw new IllegalArgumentException(string);
		}

		public static ModuleIdentifier fromValue(String v) {
			for (ModuleIdentifier m : values()) {
				if (m.value.equalsIgnoreCase(v)) {
					return m;
				}
			}
			throw new IllegalArgumentException(v);
		}

		private final String value;

		private ModuleIdentifier(String v) {
			value = v;
		}

		public String toString() {
			return super.toString();
		}
		
		public String value() {
			return value;
		}
	}
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "ENDPOINT_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ENDPOINT_SEQ")
	private Integer id;

	@Column(name = "IDENTIFIER")
	@Enumerated(EnumType.STRING)
	private ModuleIdentifier identifier;

    @Column(name = "URL")
	private String url;

	@ManyToOne
	@JoinColumn(referencedColumnName = "ID", name = "SUBSCRIPTION_ID")
	private Subscription subscription;

	public Endpoint() {

	}

	public Endpoint(ModuleIdentifier moduleIdentifier, String url) {
		this.identifier = moduleIdentifier;
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public ModuleIdentifier getIdentifier() {
		return identifier;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public String getUrl() {
		return url;
	}

	public void setIdentifier(ModuleIdentifier identifier) {
		this.identifier = identifier;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
