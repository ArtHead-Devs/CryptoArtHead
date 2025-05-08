package com.arthead.domain;

public class Repository {
    private final String name;
    private final String owner;
    private final String description;
    private final String createDate;
    private final String updateDate;
    private final String pushDate;
    private final String ts;
    private final String ss = "Github";

    public Repository(String name, String owner, String description, String createDate, String updateDate, String pushDate, String ts) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.pushDate = pushDate;
        this.ts = ts;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getPushDate() {
        return pushDate;
    }

    public String getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }
}
