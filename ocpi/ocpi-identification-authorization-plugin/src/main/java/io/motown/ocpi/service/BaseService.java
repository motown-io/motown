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
package io.motown.ocpi.service;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import io.motown.ocpi.AppConfig;
import io.motown.ocpi.persistence.repository.OcpiRepository;
import io.motown.ocpi.response.Response;
import io.motown.ocpi.response.Response.StatusCode;

/**
 * Base class for OCPI services
 * 
 * BaseService
 * 
 * @author bartwolfs
 *
 */
public class BaseService {

	protected static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Value("${io.motown.ocpi.hosturl}")
	protected String HOST_URL;

	@Value("${io.motown.ocpi.partyid}")
	protected String PARTY_ID;

	@Value("${io.motown.ocpi.countrycode}")
	protected String COUNTRY_CODE;

	@Value("${io.motown.ocpi.clientname}")
	protected String CLIENT_NAME;

	@Value("${io.motown.ocpi.token.sync.pagesize:999}")
	protected String PAGE_SIZE = "1000";

	@Autowired
	protected OcpiRepository ocpiRepository;

	/**
	 * assertValid
	 * 
	 * @param s
	 * @param message
	 */
	protected void assertValid(String s, String message) {
		if (s == null || s.length() == 0) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * 
	 * @param url
	 * @param authorizationToken
	 * @return
	 */
	protected Builder getWebResource(String url, String authorizationToken) {
		assertValid(url, "An url is required for a web resource");
		assertValid(authorizationToken, "An authorizationToken is required for web resource call");

		WebResource webResource = Client.create().resource(url);
		Builder builder = webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE);
		return builder.header("Authorization", "Token " + authorizationToken);
	}

	/**
	 * process
	 * 
	 * @param clientResponse
	 * @param successResponseClass
	 * @return
	 */
	protected Response process(ClientResponse clientResponse, Class<?> successResponseClass) {

		String entity = clientResponse.getEntity(String.class);

		try {
			Response response = (Response) OBJECT_MAPPER.readValue(entity, successResponseClass);

			if (!StatusCode.SUCCESS.equals(response.status_code)) {

				throw new RuntimeException(response.toString());
			}

			String totalCount = clientResponse.getHeaders().getFirst("X-Total-Count");
			if (totalCount != null) {
				response.totalCount = Integer.valueOf(totalCount);
			}
			return response;

		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * toJson
	 * 
	 * @param value
	 * @return json
	 */
	protected String toJson(Object value) {

		OBJECT_MAPPER.setDateFormat(AppConfig.DATE_FORMAT);
		try {
			return OBJECT_MAPPER.writeValueAsString(value);

		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
