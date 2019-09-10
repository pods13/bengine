package com.pods.bengine.content.post;

import org.springframework.beans.factory.FactoryBean;

public class PostFactory implements FactoryBean<Post> {

    @Override
    public Post getObject() {
        return new Post();
    }

    @Override
    public Class<?> getObjectType() {
        return Post.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
