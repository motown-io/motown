package io.motown.operatorapi.json;

import io.motown.operatorapi.viewmodel.spring.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import static spark.Spark.get;
import static spark.Spark.post;

public class JsonOperatorApiApplication implements SparkApplication {

    private static final Logger log = LoggerFactory.getLogger(JsonOperatorApiApplication.class);

    private OperatorApiService service;

    public JsonOperatorApiApplication() throws Exception {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        this.service = (OperatorApiService) context.getBean("operatorApiService");
    }

    @Override
    public void init() {
        post(new Route("/charging-station/boot") {
            @Override
            public Object handle(Request request, Response response) {
                service.sendBootNotification();
                return "command dispatched!";
            }
        });
        post(new Route("/charging-stations/:chargingStationId/commands") {
            @Override
            public Object handle(Request request, Response response) {
                String chargingStationId = request.params(":chargingStationId");
                int connectorId = 1;

                service.sendUnlockConnectorCommand(chargingStationId, connectorId);

                return "";
            }
        });
        get(new JsonTransformerRoute("/charging-stations") {
            @Override
            public Object handle(Request request, Response response) {
                response.type("application/json");
                return service.findAllChargingStations();
            }
        });
    }
}