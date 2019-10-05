package com.pods.bengine.content.post.warframe.primes;

import com.pods.bengine.content.post.AbstractPost;
import com.pods.bengine.content.post.matter.Matter;

public class PrimePost extends AbstractPost {

    public PrimePost() {
        super();
    }

    public PrimePost(String name, Matter matter, String content) {
        super(name, matter, content);
    }

    @Override
    public PrimePostMatter getMatter() {
        return (PrimePostMatter) super.getMatter();
    }
}
