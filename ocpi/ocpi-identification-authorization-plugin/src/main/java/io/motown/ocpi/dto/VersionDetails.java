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

import java.util.ArrayList;
import java.util.List;

/**
 * VersionDetails
 * @author bartwolfs
 *
 */
public class VersionDetails {

	public String version; // The version number.
    public List<Endpoint> endpoints = new ArrayList<Endpoint>();        // URL to the endpoint containing version specific information.
    
    public VersionDetails(){
    	
    }
}
