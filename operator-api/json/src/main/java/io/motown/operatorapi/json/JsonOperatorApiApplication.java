package io.motown.operatorapi.json;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.Connector;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.distributed.DistributedCommandBus;
import org.axonframework.commandhandling.distributed.jgroups.JGroupsConnector;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.jgroups.JChannel;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import java.util.LinkedList;
import java.util.List;

import static spark.Spark.connect;
import static spark.Spark.post;

public class JsonOperatorApiApplication implements SparkApplication {

    private CommandBus commandBus;

    public JsonOperatorApiApplication() throws Exception {
        JChannel channel = new JChannel("flush-udp.xml");
        CommandBus localSegment = new SimpleCommandBus();
        Serializer serializer = new XStreamSerializer();

        JGroupsConnector connector = new JGroupsConnector(channel, "motown.io", localSegment, serializer);
        this.commandBus = new DistributedCommandBus(connector);

        connector.connect(100);
    }

    @Override
    public void init() {
        post(new Route("/charging-station/boot") {
            @Override
            public Object handle(Request request, Response response) {
                List<Connector> connectors = new LinkedList<Connector>();
                connectors.add(new Connector(1, "CONTYPE", 32));

                commandBus.dispatch(new GenericCommandMessage<BootChargingStationCommand>(new BootChargingStationCommand("CP-001", "TUBE", connectors)));
                return "command dispatched!";
            }
        });
    }
}