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
package io.motown.ocpp.websocketjson.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingReport;
import io.motown.ocpp.websocketjson.MessageProcUri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SchemaValidator {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaValidator.class);

    private JsonSchemaFactory factory;

    /**
     * Key value is procUri (eg: BootNotification), value is JsonSchema for validation.
     */
    private Map<MessageProcUri, JsonSchema> schemas = new HashMap<>();

    public SchemaValidator() {
        factory = JsonSchemaFactory.byDefault();
    }

    public boolean isValidRequest(String request, MessageProcUri procUri) {
        JsonSchema schema = schemas.get(procUri);

        ProcessingReport report = null;

        if(schema == null) {
            try {
                String schemaName = procUri.toString().toLowerCase();
                //TODO: Prepare for other ocpp versions - Ingo Pak, 31 Mar 2014
                JsonNode fstabSchema = JsonLoader.fromResource("/schemas/v15/" + schemaName + ".json");

                schema = factory.getJsonSchema(fstabSchema);

                schemas.put(procUri, schema);
           } catch (IOException e) {
                LOG.error("IOException while loading schema for procUri: " + procUri, e);
                return false;
            } catch (ProcessingException e) {
                LOG.error("ProcessingException getting JSON schema for procUri: " + procUri, e);
                return false;
            }
        }

        try {
            JsonNode jsonNode = JsonLoader.fromString(request);
            report = schema.validate(jsonNode);
        } catch (IOException e) {
            LOG.error("IOException while loading request for validation. ProcUri: " + procUri, e);
        } catch (ProcessingException e) {
            LOG.error("ProcessingException while validating request for procUri: " + procUri, e);
        }

        return report != null && report.isSuccess();
    }

}
