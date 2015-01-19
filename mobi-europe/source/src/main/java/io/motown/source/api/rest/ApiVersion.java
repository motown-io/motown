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
package io.motown.source.api.rest;

/**
 * A set of constants describing the mobi-europe source API's version.
 * <p/>
 * Used to keep track of the vendor media MIME types used to version the mobi-europe API.
 */
public class ApiVersion {
    /**
     * Version 1 producing and consuming JSON.
     */
    public static final String V1_JSON = "application/vnd.io.motown.mobi-europe-source-api-v1+json";

    private ApiVersion() {
        // Hidden constructor to prevent instantiation of class with constants.
    }
}
