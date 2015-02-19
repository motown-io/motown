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
package io.motown.mobieurope.destination.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AuthorizationIdentifier {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationIdentifier.class);

    // Should be configurable, but this is fine for sample purpose
    // Either 8 or 12 according to the Mobi Europe Spec
    private static final int LENGTH = 12;

    private String value;

    public AuthorizationIdentifier() {
        this.value = UUID.randomUUID().toString().replace("-", "").substring(0, LENGTH);
        LOG.debug(String.format("Generated authorizationIdentifier: %s", this.value));
    }

    public String getValue() {
        return value;
    }
}
