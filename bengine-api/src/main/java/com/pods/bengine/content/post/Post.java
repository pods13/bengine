package com.pods.bengine.content.post;

import com.pods.bengine.content.post.matter.PostMatter;

public class Post extends AbstractPost {

    public Post() {
        super();
    }

    @Override
    public PostMatter getMatter() {
        return (PostMatter) super.getMatter();
    }
}
