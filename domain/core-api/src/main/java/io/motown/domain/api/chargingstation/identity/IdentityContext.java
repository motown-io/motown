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
package io.motown.domain.api.chargingstation.identity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The context in which a command is executed. Consists of add-on and user identity.
 */
public class IdentityContext {

    private AddOnIdentity addOnIdentity;

    private UserIdentity userIdentity;

    /**
     * Creates a IdentityContext with add-on and user identity.
     *
     * @param addOnIdentity
     * @param userIdentity
     * @throws NullPointerException if addOnIdentity or userIdentity is null;
     */
    public IdentityContext(AddOnIdentity addOnIdentity, UserIdentity userIdentity) {
        this.addOnIdentity = checkNotNull(addOnIdentity);
        this.userIdentity = checkNotNull(userIdentity);
    }

    /**
     * Gets the add-on identity.
     *
     * @return add-on identity.
     */
    public AddOnIdentity getAddOnIdentity() {
        return addOnIdentity;
    }

    /**
     * Gets the user identity.
     *
     * @return user identity.
     */
    public UserIdentity getUserIdentity() {
        return userIdentity;
    }

}
