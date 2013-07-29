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
