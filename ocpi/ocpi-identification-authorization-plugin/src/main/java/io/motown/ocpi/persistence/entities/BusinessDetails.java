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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * BusinessDetails entity
 * 
 * @author bartwolfs
 *
 */
@Entity
@Table(name="BUSINESS_DETAILS")
public class BusinessDetails {

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "BUSINESS_DETAILS_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "BUSINESS_DETAILS_SEQ")
	private Integer id;

    @Column(name = "NAME")
	private String name; // name of the operator
    
    @Column(name = "WEBSITE")
	private String website; // URL of the operator's website

	public BusinessDetails() {

	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getWebsite() {
		return website;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
}
