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
package io.motown.operatorapi.json.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.motown.domain.api.chargingstation.ConnectorId;
import io.motown.operatorapi.json.gson.ConnectorIdTypeAdapter;
import io.motown.operatorapi.json.gson.RequestStopTransactionTypeAdapter;
import io.motown.operatorapi.viewmodel.model.RequestStopTransactionApiCommand;

public class OperatorApiJsonTestUtils {

   public static Gson getGson() {
        return new GsonBuilder().
                setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").
                registerTypeAdapter(RequestStopTransactionApiCommand.class, new RequestStopTransactionTypeAdapter()).
                registerTypeAdapter(ConnectorId.class, new ConnectorIdTypeAdapter()).
                create();
    }

}
