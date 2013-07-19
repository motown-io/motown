package io.motown.operatorapi.json;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.Connector;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.operatorapi.viewmodel.spring.ApplicationContextProvider;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static spark.Spark.get;
import static spark.Spark.post;

public class JsonOperatorApiApplication implements SparkApplication {

    private static final Logger log = LoggerFactory.getLogger(JsonOperatorApiApplication.class);

    private CommandBus commandBus;

    private ChargingStationRepository repository;

    private Random random;

    public JsonOperatorApiApplication() throws Exception {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        this.commandBus = (CommandBus) context.getBean("commandBus");
        this.repository = (ChargingStationRepository) context.getBean("chargingStationRepository");
        this.random = new Random();
    }

    @Override
    public void init() {
        post(new Route("/charging-station/boot") {
            @Override
            public Object handle(Request request, Response response) {
                List<Connector> connectors = new LinkedList<Connector>();
                connectors.add(new Connector(1, "CONTYPE", 32));

                commandBus.dispatch(new GenericCommandMessage<BootChargingStationCommand>(new BootChargingStationCommand(new ChargingStationId("CP-0" + random.nextInt(100)), "TUBE", connectors)));
                return "command dispatched!";
            }
        });
        get(new JsonTransformerRoute("/charging-stations") {
            @Override
            public Object handle(Request request, Response response) {
                response.type("application/json");
                return repository.findAll();
            }
        });
    }
}