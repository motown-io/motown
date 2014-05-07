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
package io.motown.chargingstationconfiguration.viewmodel.restapi.response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Navigation item to be returned in the Operator API response.
 */
public final class NavigationItem {
    private final String href;

    /**
     * Construct a navigation item containing a link.
     * @param href  the link for this navigation item.
     */
    public NavigationItem(String href) {
        this.href = checkNotNull(href);
    }

    public String getHref() {
        return href;
    }
}
