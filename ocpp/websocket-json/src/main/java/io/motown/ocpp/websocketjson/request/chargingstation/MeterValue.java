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
package io.motown.ocpp.websocketjson.request.chargingstation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeterValue {

    private Date timestamp;

    private List<MeterValue.Value> values = new ArrayList<>();

    public MeterValue(Date timestamp, List<Value> values) {
        this.timestamp = timestamp != null ? new Date(timestamp.getTime()) : null;
        this.values = values;
    }

    public Date getTimestamp() {
        return timestamp != null ? new Date(timestamp.getTime()) : null;
    }

    public List<Value> getValues() {
        return values;
    }

    public static class Value {

        private String value;

        private String context;

        private String format;

        private String measurand;

        private String location;

        private String unit;

        public Value(String value, String context, String format, String measurand, String location, String unit) {
            this.value = value;
            this.context = context;
            this.format = format;
            this.measurand = measurand;
            this.location = location;
            this.unit = unit;
        }

        public String getValue() {
            return value;
        }

        public String getContext() {
            return context;
        }

        public String getFormat() {
            return format;
        }

        public String getMeasurand() {
            return measurand;
        }

        public String getLocation() {
            return location;
        }

        public String getUnit() {
            return unit;
        }

    }

}
