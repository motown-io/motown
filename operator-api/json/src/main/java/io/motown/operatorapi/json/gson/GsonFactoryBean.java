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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.FactoryBean;

import java.util.Set;

public class GsonFactoryBean implements FactoryBean<Gson> {

    private String dateFormat;

    private Set<CommandTypeAdapter<?>> typeAdapters;

    @Override
    public Gson getObject() throws Exception {
        GsonBuilder builder = new GsonBuilder();

        if (dateFormat != null && !dateFormat.isEmpty()) {
            builder.setDateFormat(dateFormat);
        }

        for (CommandTypeAdapter<?> typeAdapter : typeAdapters) {
            builder.registerTypeAdapter(typeAdapter.getAdaptedType(), typeAdapter);
        }

        return builder.create();
    }

    @Override
    public Class<?> getObjectType() {
        return Gson.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setTypeAdapters(Set<CommandTypeAdapter<?>> typeAdapters) {
        this.typeAdapters = typeAdapters;
    }
}
