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
package io.motown.operatorapi.json;

import io.motown.operatorapi.json.commands.JsonCommandService;
import io.motown.operatorapi.json.queries.OperatorApiService;
import io.motown.operatorapi.json.spark.JsonTransformerRoute;
import io.motown.operatorapi.viewmodel.spring.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import java.net.HttpURLConnection;

import static spark.Spark.get;
import static spark.Spark.post;

public class JsonOperatorApiApplication implements SparkApplication {

    private OperatorApiService service;

    private JsonCommandService commandService;

    private String baseUrl;

    public JsonOperatorApiApplication() {
        // TODO refactor this so application context is not needed - Mark van den Bergh, Februari 26th 2014
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        this.service = (OperatorApiService) context.getBean("operatorApiService");
        this.commandService = (JsonCommandService) context.getBean("jsonCommandService");
        this.baseUrl = (String) (context.containsBean("operatorApiBaseUrl")?context.getBean("operatorApiBaseUrl"):"");
    }

    @Override
    public void init() {
        post(new Route(baseUrl + "/charging-stations/:chargingStationId/commands") {
            @Override
            public Object handle(Request request, Response response) {
                String chargingStationId = request.params(":chargingStationId");

                commandService.handleCommand(chargingStationId, request.body());

                response.status(HttpURLConnection.HTTP_ACCEPTED);
                return "";
            }
        });
        get(new JsonTransformerRoute(baseUrl + "/charging-stations") {
            @Override
            public Object handle(Request request, Response response) {
                response.type("application/json");
                return service.findAllChargingStations();
            }
        });
        get(new JsonTransformerRoute(baseUrl + "/transactions") {
            @Override
            public Object handle(Request request, Response response) {
                response.type("application/json");
                return service.findAllTransactions();
            }
        });
    }
}
