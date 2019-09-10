package com.pods.bengine.content.post;

import com.pods.bengine.content.post.matter.Matter;

public abstract class AbstractPost {

    protected String name;

    protected Matter matter;

    protected String content;

    public AbstractPost() {

    }

    public AbstractPost(String name, Matter matter, String content) {
        this.name = name;
        this.matter = matter;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Matter getMatter() {
        return matter;
    }

    public void setMatter(Matter matter) {
        this.matter = matter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
