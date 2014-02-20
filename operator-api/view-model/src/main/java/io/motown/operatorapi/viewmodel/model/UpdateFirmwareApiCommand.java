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

import java.util.Date;

public class UpdateFirmwareApiCommand implements ApiCommand {
    private String location;
    private Date retrieveDate;

    public UpdateFirmwareApiCommand() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // TODO should the Date fields really be copies? - Mark Manders 2014-02-20
    public Date getRetrieveDate() {
        return retrieveDate != null ? new Date(retrieveDate.getTime()) : null;
    }

    public void setRetrieveDate(Date retrieveDate) {
        this.retrieveDate = retrieveDate != null ? new Date(retrieveDate.getTime()) : null;
    }
}
