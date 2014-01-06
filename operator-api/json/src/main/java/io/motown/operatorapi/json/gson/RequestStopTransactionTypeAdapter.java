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
package io.motown.operatorapi.json.gson;

import com.google.gson.*;
import io.motown.operatorapi.viewmodel.model.RequestStopTransactionApiCommand;

import static com.google.common.base.Preconditions.checkArgument;

public class RequestStopTransactionTypeAdapter extends CommandTypeAdapter<RequestStopTransactionApiCommand> {

    private static final String TRANSACTION_ID_FIELD = "id";

    @Override
    public RequestStopTransactionApiCommand deserialize(JsonObject commandObject, JsonDeserializationContext context) throws JsonParseException {
        checkArgument(commandObject.has(TRANSACTION_ID_FIELD), String.format("Given object does not have field [%s]", TRANSACTION_ID_FIELD));
        JsonElement transactionIdAsElement = commandObject.get(TRANSACTION_ID_FIELD);

        checkArgument(transactionIdAsElement.isJsonPrimitive(), String.format("Field [%s] must be a JSON primitive", TRANSACTION_ID_FIELD));
        String transactionId = commandObject.get(TRANSACTION_ID_FIELD).getAsString();

        return new RequestStopTransactionApiCommand(transactionId);
    }

    @Override
    public Class<?> getAdaptedType() {
        return RequestStopTransactionTypeAdapter.class;
    }
}
