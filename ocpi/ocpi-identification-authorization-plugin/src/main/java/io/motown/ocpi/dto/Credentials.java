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
package io.motown.ocpi.dto;

/**
 * Credentials
 * 
 * @author bartwolfs
 *
 */
public class Credentials {

	public String token; // The token for the other party to authenticate in Lukas
	public String url; // URL to the 'versions' endpoint
	public BusinessDetails business_details; // Details of the other party
	public String party_id; // CPO or eMSP ID of this party. (following the
							// 15118 ISO standard).
	public String country_code; // Country code of the country this party is operating in.

	public Credentials() {

	}

}
