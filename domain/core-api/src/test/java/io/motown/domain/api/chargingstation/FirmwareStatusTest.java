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

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class FirmwareStatusTest {

    @RunWith(Parameterized.class)
    public static class FromValueTest {
        private FirmwareStatus firmwareStatus;

        private String value;

        public FromValueTest(FirmwareStatus firmwareStatus, String value) {
            this.firmwareStatus = firmwareStatus;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {FirmwareStatus.DOWNLOAD_FAILED, "DownloadFailed"},
                    {FirmwareStatus.DOWNLOADED, "Downloaded"},
                    {FirmwareStatus.INSTALLATION_FAILED, "InstallationFailed"},
                    {FirmwareStatus.INSTALLED, "Installed"},
                    {FirmwareStatus.DOWNLOAD_FAILED, "DOWNLOADFAILED"},
                    {FirmwareStatus.DOWNLOADED, "DOWNLOADED"},
                    {FirmwareStatus.INSTALLATION_FAILED, "INSTALLATIONFAILED"},
                    {FirmwareStatus.INSTALLED, "INSTALLED"}
            });
        }

        @Test
        public void fromValueShouldReturnTheEnumForValue() {
            assertEquals(firmwareStatus, FirmwareStatus.fromValue(value));
        }

        @Test(expected = IllegalArgumentException.class)
        public void fromValueWithUnknownFirmwareStatusShouldThrowIllegalArgumentException() {
            FirmwareStatus.fromValue("NonExistentFirmwareStatus");
        }

        @Test(expected = NullPointerException.class)
        public void fromValueWithFirmwareStatusNullShouldThrowNullPointerException() {
            ComponentStatus.fromValue(null);
        }

        @Test
        public void toStringShouldReturnValue() {
            assertTrue(firmwareStatus.toString().equalsIgnoreCase(value));
        }
    }

    @RunWith(Parameterized.class)
    public static class ToStringTest {
        private FirmwareStatus firmwareStatus;

        private String value;

        public ToStringTest(FirmwareStatus firmwareStatus, String value) {
            this.firmwareStatus = firmwareStatus;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {FirmwareStatus.DOWNLOAD_FAILED, "DownloadFailed"},
                    {FirmwareStatus.DOWNLOADED, "Downloaded"},
                    {FirmwareStatus.INSTALLATION_FAILED, "InstallationFailed"},
                    {FirmwareStatus.INSTALLED, "Installed"}
            });
        }

        @Test
        public void toStringShouldReturnValue() {
            assertTrue(firmwareStatus.toString().equals(value));
        }
    }
}
