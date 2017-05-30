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
package io.motown.ocpi.response;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnore;

import io.motown.ocpi.AppConfig;

/**
 * Generic Json Response
 * 
 * Response
 * 
 * @author bartwolfs
 *
 */
public class Response {

	public enum StatusCode {

	    SUCCESS(1000),
	    CLIENT_ERROR(2000),
	    INVALID_OR_MISSING_PARAMETERS(2001),
	    GENERIC_SERVER_ERROR(3000),
	    UNABLE_TO_USE_CLIENT_API(3001),
	    UNSUPPORTED_VERSION(3002);

	    /**
	     * Return the enum constant of this type with the specified numeric value.
	     * @param statusCode the numeric value of the enum to be returned
	     * @return the enum constant with the specified numeric value
	     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
	     */
	    public static StatusCode valueOf(int statusCode) {
	        for (StatusCode status : values()) {
	            if (status.value == statusCode) {
	                return status;
	            }
	        }
	        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	    }

	    private final int value;

	    private StatusCode(int value) {
	        this.value = value;
	    }

	    /**
	     * Return a string representation of this status code.
	     */
	    @Override
	    public String toString() {
	        return Integer.toString(value);
	    }

	    /**
	     * Return the integer value of this status code.
	     */
	    public int value() {
	        return this.value;
	    }

	    public boolean equals(Integer code){
	    	return this.value == code;
	    }
	    
	}

	@JsonIgnore
	public Integer totalCount;

	public Integer status_code;

	public String status_message;
	
	public String timestamp;

	public Response() {
		
	}
	public Response(StatusCode status){
		this(status, status.name());
	}
	public Response(StatusCode status, String message)	{
		this.status_code = status.value;
		this.status_message = message;
		this.timestamp = AppConfig.DATE_FORMAT.format(new Timestamp(System.currentTimeMillis()));
	}
	
	public String toString(){
		return timestamp + " " + this.getClass().getName() + "; status: " + status_code + "; message: " + status_message; 
	}
}
