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

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import javax.annotation.Nullable;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DiagnosticsRequestedEvent} is the event which is published when a charging station's diagnostics are requested.
 */
public final class DiagnosticsRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final String uploadLocation;

    private Integer numRetries;

    private Integer retryInterval;

    private Date periodStartTime;

    private Date periodStopTime;

    /**
     * Creates a {@code DiagnosticsRequestedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol the charging station supports
     * @param uploadLocation    the location where the diagnostics file should be uploaded to
     *
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, or {@code uploadLocation} is {@code null}.
     */
    public DiagnosticsRequestedEvent(ChargingStationId chargingStationId, String protocol, String uploadLocation) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        this.uploadLocation = checkNotNull(uploadLocation);
    }

    /**
     * Creates a {@code DiagnosticsRequestedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol the charging station supports
     * @param uploadLocation    the location where the diagnostics file should be uploaded to
     * @param numRetries        the optional number of retries the charging station should perform in case of failure
     * @param retryInterval     the optional interval in seconds between retry attempts
     * @param periodStartTime   the optional date and time of the oldest logging information to include in the diagnostics report
     * @param periodStopTime     the optional date and time of the latest logging information to include in the diagnostics report
     *
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, or {@code uploadLocation} is {@code null}.
     */
    public DiagnosticsRequestedEvent(ChargingStationId chargingStationId, String protocol, String uploadLocation, @Nullable Integer numRetries, @Nullable Integer retryInterval, @Nullable Date periodStartTime, @Nullable Date periodStopTime) {
        this(chargingStationId, protocol, uploadLocation);

        this.numRetries = numRetries;
        this.retryInterval = retryInterval;
        this.periodStartTime = periodStartTime;
        this.periodStopTime = periodStopTime;
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * @return the protocol identifier
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     *
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
        return periodStartTime;
    }

    /**
     * @return the optional date and time of the latest logging information to include in the diagnostics report
     */
    @Nullable
    public Date getPeriodStopTime() {
        return periodStopTime;
    }
}
