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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The {@code FirmwareStatus} represents the status of a firmware update.
 */
public enum FirmwareStatus {
    /**
     * The firmware update download was successfully completed.
     */
    DOWNLOADED("Downloaded"),

    /**
     * The firmware update download failed.
     */
    DOWNLOAD_FAILED("DownloadFailed"),

    /**
     * The firmware update installation failed.
     */
    INSTALLATION_FAILED("InstallationFailed"),

    /**
     * The firmware update installation was successfully completed.
     */
    INSTALLED("Installed");

    private final String value;

    private FirmwareStatus(String v) {
        value = v;
    }

    /**
     * Gets a {@code FirmwareStatus} from a {@code String} value.
     *
     * @param value a {@code String} value representing one of the statuses.
     * @return the {@code FirmwareStatus}.
     * @throws NullPointerException     if value is null.
     * @throws IllegalArgumentException if value is not one of the known statuses.
     */
    public static FirmwareStatus fromValue(String value) {
        checkNotNull(value);

        for (FirmwareStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("FirmwareStatus value must be one of the known statuses");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return value;
    }
}
