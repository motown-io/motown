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

import com.google.common.collect.ImmutableMap;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code StatusNotification} is the command which is published when a charging station notifies Motown about
 * some status. Specific commands will indicate which status is referred to.
 */
public class StatusNotification {

    private final ComponentStatus status;

    private final Date timeStamp;

    private final Map<String, String> attributes;

    /**
     * Creates a {@code StatusNotification} with ComponentStatus, TimeStamp and optional attributes.
     *
     * @throws NullPointerException if {@code status} or {@code timeStamp} is {@code null}.
     */
    public StatusNotification(ComponentStatus status, Date timeStamp, Map<String, String> attributes) {
        this.status = checkNotNull(status);
        this.timeStamp = new Date(checkNotNull(timeStamp).getTime());
        this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
    }

    public ComponentStatus getStatus() {
        return status;
    }

    public Date getTimeStamp() {
        return new Date(timeStamp.getTime());
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, timeStamp, attributes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StatusNotification other = (StatusNotification) obj;
        return Objects.equals(this.status, other.status) && Objects.equals(this.timeStamp, other.timeStamp) && Objects.equals(this.attributes, other.attributes);
    }

    @Override
    public String toString() {
        return "StatusNotification{" +
                "status=" + status +
                ", timeStamp=" + timeStamp +
                ", attributes=" + attributes +
                '}';
    }
}
