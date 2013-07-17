package io.motown.operatorapi.json;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.Connector;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.distributed.DistributedCommandBus;
import org.axonframework.commandhandling.distributed.jgroups.JGroupsConnector;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.jgroups.JChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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

    private EventBus eventBus;

    private ChargingStationRepository repository;

    private Random random;

    public JsonOperatorApiApplication() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/view-model-context.xml");
        this.eventBus = (EventBus) context.getBean("eventBus");

        JChannel channel = new JChannel("flush-udp.xml");
        CommandBus localSegment = new SimpleCommandBus();
        Serializer serializer = new XStreamSerializer();

        JGroupsConnector connector = new JGroupsConnector(channel, "motown.io", localSegment, serializer);
        this.commandBus = new DistributedCommandBus(connector);

        connector.connect(100);

        this.repository = (ChargingStationRepository) context.getBeansOfType(ChargingStationRepository.class).values().toArray()[0];

        this.random = new Random();
    }

    @Override
    public void init() {
        post(new Route("/charging-station/boot") {
            @Override
            public Object handle(Request request, Response response) {
                List<Connector> connectors = new LinkedList<Connector>();
                connectors.add(new Connector(1, "CONTYPE", 32));

                commandBus.dispatch(new GenericCommandMessage<BootChargingStationCommand>(new BootChargingStationCommand("CP-0" + random.nextInt(100), "TUBE", connectors)));
                return "command dispatched!";
            }
        });
        get(new Route("/charging-stations") {
            @Override
            public Object handle(Request request, Response response) {
                return repository.findAll();
            }
        });
    }
}