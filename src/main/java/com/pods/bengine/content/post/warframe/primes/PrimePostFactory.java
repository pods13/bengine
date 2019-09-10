package com.pods.bengine.content.post.warframe.primes;

import org.springframework.beans.factory.FactoryBean;

public class PrimePostFactory implements FactoryBean<PrimePost> {

    @Override
    public PrimePost getObject() {
        return new PrimePost();
    }

    @Override
    public Class<?> getObjectType() {
        return PrimePost.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
