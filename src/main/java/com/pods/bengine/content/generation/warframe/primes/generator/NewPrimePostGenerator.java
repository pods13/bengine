package com.pods.bengine.content.generation.warframe.primes.generator;

import com.pods.bengine.content.generation.warframe.primes.template.GeneratedPrimeItem;
import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import com.pods.bengine.content.post.matter.Matter;
import com.pods.bengine.content.post.warframe.primes.PrimePostMatter;
import org.springframework.stereotype.Service;
import org.trimou.engine.MustacheEngine;

@Service
public class NewPrimePostGenerator extends PrimePostGenerator {

    public NewPrimePostGenerator(MustacheEngine mustacheEngine) {
        super(mustacheEngine);
    }

    @Override
    protected Matter generateFrontMatter(GeneratedPrimeItem primeItem) {
        PrimePostMatter matter = (PrimePostMatter) super.generateFrontMatter(primeItem);
        matter.setSeoTitle(String.format("How To Get %1$s. %1$s Relics Farm.", primeItem.getName()));
        return matter;
    }

    @Override
    public PrimePostStatus getStatus() {
        return PrimePostStatus.NEW;
    }
}