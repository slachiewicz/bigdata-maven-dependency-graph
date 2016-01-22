package nl.ordina.jtech.mavendependencygraph.model;

import com.google.gson.Gson;

import java.io.Serializable;

interface GSonConverter extends Serializable{
    Gson gson = new Gson();

    default String toJson() {
        return gson.toJson(this);
    }

    static <T> T fromJson(final String input, Class<T> classOfT) {
        return gson.fromJson(input, classOfT);
    }
}
