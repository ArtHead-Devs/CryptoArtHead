package com.arthead.model;

public class Information {
    private int stars;
    private int forks;
    private int issues;
    private int watchers;

    public Information(int forks, int open_issues_count, int stargazers_count, int subscribers_count) {
        this.forks = forks;
        this.issues = open_issues_count;
        this.stars = stargazers_count;
        this.watchers = subscribers_count;
    }

    public int getForks() {
        return forks;
    }

    public int getIssues() {
        return issues;
    }

    public int getStars() {
        return stars;
    }

    public int getWatchers() {
        return watchers;
    }
}
