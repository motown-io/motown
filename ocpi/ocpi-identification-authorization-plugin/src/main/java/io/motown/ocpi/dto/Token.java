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

import io.motown.ocpi.persistence.entities.Token.TokenType;
import io.motown.ocpi.persistence.entities.Token.WhitelistType;

/**
 * Token
 * @author bartwolfs
 *
 */
public class Token {

    public String uid;              // Identification used by CPO system to identify this token,
    public TokenType type;          // Type of the token
    public String auth_id;          // Uniquely identifies the EV Driver
    public String visual_number;    // Visual readable number/identification of the Token
    public String issuer;           // Issuing company
    public Boolean valid;           // Is this Token valid
    public WhitelistType whitelist; // Indicates what type of white listing is allowed
    public String languageCode;     // Language Code ISO 639-1
    public String last_updated;
    
    public Token(){
    	
    }
}
