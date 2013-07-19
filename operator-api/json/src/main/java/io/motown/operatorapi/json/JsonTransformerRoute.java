package io.motown.operatorapi.json;

import com.google.gson.Gson;
import spark.ResponseTransformerRoute;

public abstract class JsonTransformerRoute extends ResponseTransformerRoute {

    private static final String ACCEPT_TYPE = "application/json";

    private Gson gson;

    protected JsonTransformerRoute(String path) {
        super(path, ACCEPT_TYPE);

        this.gson = new Gson();
    }

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
