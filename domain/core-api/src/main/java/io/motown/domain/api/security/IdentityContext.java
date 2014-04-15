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
package io.motown.domain.api.security;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The context in which an action is performed. Consists of add-on and user identity.
 */
public class IdentityContext {

    private AddOnIdentity addOnIdentity;

    private UserIdentity userIdentity;

    /**
     * Creates a IdentityContext with add-on and user identity.
     *
     * @param addOnIdentity add-on identity
     * @param userIdentity user identity
     * @throws NullPointerException if typeBasedAddOnIdentity or userIdentity is null;
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
    public AddOnIdentity getTypeBasedAddOnIdentity() {
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

    @Override
    public String toString() {
        return "IdentityContext{" +
                "addOnIdentity=" + addOnIdentity +
                ", userIdentity=" + userIdentity +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(addOnIdentity, userIdentity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final IdentityContext other = (IdentityContext) obj;
        return Objects.equals(this.addOnIdentity, other.addOnIdentity) && Objects.equals(this.userIdentity, other.userIdentity);
    }
}
