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
import java.io.InputStream;
import java.io.StringWriter;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.net.HttpHeaders;

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

	private static final Logger LOG = LoggerFactory.getLogger(BaseService.class);

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

	protected OcpiRepository ocpiRepository;

	public void setOcpiRepository(OcpiRepository ocpiRepository) {
		this.ocpiRepository = ocpiRepository;
	}

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
	 * doRequest
	 * 
	 * @param request
	 * @param authorizationToken
	 * @param successResponseClass
	 * @return
	 */
	protected Response doRequest(HttpRequestBase request, String authorizationToken, Class<?> successResponseClass) {

		assertValid(authorizationToken, "An authorizationToken is required for web resource call");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		request.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
		request.setHeader("Authorization", "Token " + authorizationToken);
		
		try {
			CloseableHttpResponse clientResponse = httpclient.execute(request);
		
		    HttpEntity entity = clientResponse.getEntity();

		    InputStream inputStream = entity.getContent();

		    StringWriter writer = new StringWriter();
		    IOUtils.copy(inputStream, writer, "UTF-8");
		    String json = writer.toString();
		    
		    LOG.debug("json: " + json);
		    
			Response response = (Response) OBJECT_MAPPER.readValue(json, successResponseClass);

			LOG.debug("response.statuscode: " + response.status_code);
			
			if (!StatusCode.SUCCESS.equals(response.status_code)) {

				throw new RuntimeException(response.toString());
			}

			Header totalCount = clientResponse.getFirstHeader("X-Total-Count");
			if (totalCount != null) {
				response.totalCount = Integer.valueOf(totalCount.getValue());
			}
		    EntityUtils.consume(entity);
			clientResponse.close();
		    
			LOG.debug("Response: " + response);
		    
			return response;

		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
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
