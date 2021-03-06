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
package io.motown.domain.api.chargingstation;

import javax.annotation.Nullable;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class DiagnosticsUploadSettings {

    private final String uploadLocation;

    private final Integer numRetries;

    private final Integer retryInterval;

    private final Date periodStartTime;

    private final Date periodStopTime;

    /**
     *
     * @param uploadLocation    the location to upload the diagnostics file to
     */
    public DiagnosticsUploadSettings(String uploadLocation) {
        this(uploadLocation, null, null, null, null);
    }

    /**
     *
     * @param uploadLocation    the location to upload the diagnostics file to
     * @param numRetries        the number of retries the charging station should attempt to deliver the diagnostics file
     * @param retryInterval     the time in seconds between the retry attempts
     * @param periodStartTime   the date/time of the oldest diagnostics information that should be contained in the diagnostics report
     * @param periodStopTime    the date/time of the latest diagnostics information that should be contained in the diagnostics report
     */
    public DiagnosticsUploadSettings(String uploadLocation, @Nullable Integer numRetries, @Nullable Integer retryInterval,
                                     @Nullable Date periodStartTime, @Nullable Date periodStopTime) {
        this.uploadLocation = checkNotNull(uploadLocation);
        this.numRetries = numRetries;
        this.retryInterval = retryInterval;
        this.periodStartTime = periodStartTime != null ? new Date(periodStartTime.getTime()) : null;
        this.periodStopTime = periodStopTime != null ? new Date(periodStopTime.getTime()) : null;
    }

    /**
     * @return the location where the diagnostics file should be uploaded to
     */
    public String getUploadLocation() {
        return uploadLocation;
    }

    /**
     * @return the optional number of retries to perform in case the upload fails
     */
    @Nullable
    public Integer getNumRetries() {
        return numRetries;
    }

    /**
     * @return the optional amount of time in seconds to wait before performing a retry
     */
    @Nullable
    public Integer getRetryInterval() {
        return retryInterval;
    }

    /**
     * @return the optional date and time of the oldest logging information to include in the diagnostics report
     */
    @Nullable
    public Date getPeriodStartTime() {
        return periodStartTime != null ? new Date(periodStartTime.getTime()) : null;
    }

    /**
     * @return the optional date and time of the latest logging information to include in the diagnostics report
     */
    @Nullable
    public Date getPeriodStopTime() {
        return periodStopTime != null ? new Date(periodStopTime.getTime()) : null;
    }

}
