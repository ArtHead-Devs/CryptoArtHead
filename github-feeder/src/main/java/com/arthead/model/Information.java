package com.arthead.model;

public class Information {
    private final String name;
    private final int stars;
    private final int forks;
    private final int issuesAndPullRequest;
    private final int watchers;
    private final String ts;
    private final String ss = "Github";

    public Information(String name, int stars, int forks, int issuesAndPullRequest, int watchers, String ts) {
        this.name = name;
        this.stars = stars;
        this.forks = forks;
        this.issuesAndPullRequest = issuesAndPullRequest;
        this.watchers = watchers;
        this.ts = ts;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }

    public int getForks() {
        return forks;
    }

    public int getIssuesAndPullRequest() {
        return issuesAndPullRequest;
    }

    public int getWatchers() {
        return watchers;
    }

    public String getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
