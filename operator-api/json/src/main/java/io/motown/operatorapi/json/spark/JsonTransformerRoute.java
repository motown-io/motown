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
package io.motown.operatorapi.json.spark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformerRoute;

public abstract class JsonTransformerRoute extends ResponseTransformerRoute {

    private static final String ACCEPT_TYPE = "application/json";

    private Gson gson;

    protected JsonTransformerRoute(String path) {
        super(path, ACCEPT_TYPE);

        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
    }

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
