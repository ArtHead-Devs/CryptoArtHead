package com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider;

import com.arthead.githubfeeder.domain.GithubData;
import com.arthead.githubfeeder.domain.Information;
import com.arthead.githubfeeder.domain.Repository;
import com.arthead.githubfeeder.util.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.time.Instant;

public class GithubDeserializer {
    private final Gson gson = new Gson();

    public GithubData deserialize(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Information information = parseInformation(jsonObject);
        Repository repository = parseRepository(jsonObject);
        return new GithubData(information, repository);
    }

    private Information parseInformation(JsonObject jsonObject){
        String name = jsonObject.get("name").getAsString();
        int stars = jsonObject.get("stargazers_count").getAsInt();
        int forks = jsonObject.get("forks_count").getAsInt();
        int IssuesAndPullRequest = jsonObject.get("open_issues_count").getAsInt();
        int watchers = jsonObject.get("subscribers_count").getAsInt();
        String ts = Instant.now().toString();

        return new Information(name, stars, forks, IssuesAndPullRequest, watchers, ts);
    }

    private Repository parseRepository(JsonObject jsonObject){
        String name = jsonObject.get("name").getAsString();
        String owner = jsonObject.getAsJsonObject("owner").get("login").getAsString();
        String description = JsonHelper.getString(jsonObject, "description");
        String createDate = Instant.parse(jsonObject.get("created_at").getAsString()).toString();
        String updateDate = Instant.parse(jsonObject.get("updated_at").getAsString()).toString();
        String pushedDate = Instant.parse(jsonObject.get("pushed_at").getAsString()).toString();
        String ts = Instant.now().toString();

        return new Repository(name, owner, description, createDate, updateDate, pushedDate, ts);
    }
}
