package com.arthead.controller;

import com.arthead.model.Information;
import com.arthead.model.Owner;
import com.arthead.model.Repository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GithubDeserializer {
    private final Gson gson = new Gson();
    private final SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Repository deserialize(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        try {
            // Convertir las fechas del JSON a objetos Date
            Date createDate = iso8601Format.parse(jsonObject.get("created_at").getAsString());
            Date updateDate = iso8601Format.parse(jsonObject.get("updated_at").getAsString());
            Date pushDate = iso8601Format.parse(jsonObject.get("pushed_at").getAsString());

            return new Repository(
                    jsonObject.get("name").getAsString(),
                    gson.fromJson(jsonObject.get("owner"), Owner.class),
                    jsonObject.has("description") && !jsonObject.get("description").isJsonNull()
                            ? jsonObject.get("description").getAsString()
                            : null,
                    createDate,
                    updateDate,
                    pushDate,
                    new Information(
                            jsonObject.get("forks_count").getAsInt(),
                            jsonObject.get("open_issues_count").getAsInt(),
                            jsonObject.get("stargazers_count").getAsInt(),
                            jsonObject.get("subscribers_count").getAsInt()
                    )
            );
        } catch (ParseException e) {
            throw new RuntimeException("Error al parsear las fechas del JSON", e);
        }
    }
}
