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
package io.motown.operatorapi.viewmodel.model;

import io.motown.domain.api.chargingstation.TextualToken;

import java.util.Set;

public class SendAuthorizationListApiCommand implements ApiCommand {

    private Set<TextualToken> items;
    private Integer listVersion;
    private String updateType;

    public SendAuthorizationListApiCommand() {
    }

    public Set<TextualToken> getItems() {
        return items;
    }

    public void setItems(Set<TextualToken> items) {
        this.items = items;
    }

    public Integer getListVersion() {
        return listVersion;
    }

    public void setListVersion(Integer listVersion) {
        this.listVersion = listVersion;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

}
