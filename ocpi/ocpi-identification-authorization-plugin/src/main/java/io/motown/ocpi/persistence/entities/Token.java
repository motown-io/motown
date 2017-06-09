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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Token entity class
 * 
 * @author bartwolfs
 *
 */
@Entity
@Table(name = "TOKEN")
public class Token {

	public enum TokenType {
		RFID, OTHER
	}

	public enum WhitelistType {
		ALWAYS, ALLOWED, ALLOWED_OFFLINE, NEVER
	}

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "TOKEN_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "TOKEN_SEQ")
	private Integer id;

	@Column(name = "AUTH_ID")
	private String authId; // Uniquely identifies the EV Driver

	@Column(name = "TOKEN_UID")
	private String uid; // Identification used by CPO system to identify this
						// token,

	@Column(name = "token_type")
	@Enumerated(EnumType.STRING)
	private TokenType tokenType; // Type of the token

	@Column(name = "VISUAL_NUMBER")
	private String visualNumber; // Visual readable number/identification of the
									// Token

	@Column(name = "ISSUING_COMPANY")
	private String issuingCompany; // Issuing company

	@Column(name = "VALID")
	private Boolean valid; // Is this Token valid

	@Column(name = "WHITELIST")
	@Enumerated(EnumType.STRING)
	private WhitelistType whitelist; // Indicates what type of white listing is
										// allowed

	@Column(name = "LANGUAGE_CODE")
	private String languageCode; // Language Code ISO 639-1

	@Column(name = "DATE_CREATED", nullable = false)
	private Date dateCreated;

	@Column(name = "LAST_UPDATED")
	private Date lastUpdated;

	@Column(name = "SUBSCRIPTION_ID", nullable = false)
	private Integer subscriptionId;
	
	public Token() {

	}

	public String getAuthId() {
		return authId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Integer getId() {
		return id;
	}

	public String getIssuingCompany() {
		return issuingCompany;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public Integer getSubscriptionId() {
		return subscriptionId;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public String getUid() {
		return uid;
	}

	public Boolean getValid() {
		return valid;
	}

	public String getVisualNumber() {
		return visualNumber;
	}

	public WhitelistType getWhitelist() {
		return whitelist;
	}

	public Boolean isValid() {
		return uid != null && visualNumber != null && issuingCompany != null && valid;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIssuingCompany(String issuingCompany) {
		this.issuingCompany = issuingCompany;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public void setVisualNumber(String visualNumber) {
		this.visualNumber = visualNumber;
	}

	public void setWhitelist(WhitelistType whitelist) {
		this.whitelist = whitelist;
	}

}
