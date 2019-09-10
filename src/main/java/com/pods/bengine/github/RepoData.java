package com.pods.bengine.github;

public class RepoData {

    private String name;
    private String owner;
    private String branch;
    private String pathPrefix;


    public RepoData(String name, String owner, String branch, String pathPrefix) {
        this.name = name;
        this.owner = owner;
        this.branch = branch;
        this.pathPrefix = pathPrefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }
}
