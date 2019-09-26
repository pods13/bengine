package com.pods.bengine.content.generation.warframe.primes.generator;

import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import com.pods.bengine.content.generation.warframe.primes.template.GeneratedPrimeItem;
import com.pods.bengine.content.post.matter.Matter;
import com.pods.bengine.content.post.warframe.primes.PrimePost;
import com.pods.bengine.content.post.warframe.primes.PrimePostMatter;
import org.trimou.Mustache;
import org.trimou.engine.MustacheEngine;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

public abstract class PrimePostGenerator {

    private final MustacheEngine mustacheEngine;

    protected PrimePostGenerator(MustacheEngine mustacheEngine) {
        this.mustacheEngine = mustacheEngine;
    }

    public PrimePost generate(GeneratedPrimeItem primeItem) {
        String name = generateFileName(primeItem);
        Matter matter = generateFrontMatter(primeItem);
        String content = generateContent(primeItem);
        return new PrimePost(name, matter, content);
    }

    private String generateFileName(GeneratedPrimeItem primeItem) {
        return "how-to-get-" + primeItem.getNormalizedName() + ".md";
    }

    protected Matter generateFrontMatter(GeneratedPrimeItem primeItem) {
        PrimePostMatter matter = new PrimePostMatter();
        matter.setGroupId(primeItem.getGroupId());
        matter.setTitle("How To Get " + primeItem.getName());
        matter.setDate(Date.valueOf(LocalDate.now().minusDays(5)));
        matter.setAuthor("warframe");
        matter.setLayout("post");
        matter.setPermalink("/primes/" + "how-to-get-" + primeItem.getNormalizedName() + "/");
        matter.setCategories(new String[]{"Primes"});
        matter.setItemName(primeItem.getName());
        matter.setAlongWithPrimeItems(Arrays.asList(primeItem.getAlongWithPrimeItems()));
        matter.setImage("/images/primes/warframe-how-to-get-" + primeItem.getNormalizedName() + ".jpg");
        matter.setStatus(getStatus());
        matter.setDraft(primeItem.isDraft());
        return matter;
    }

    private String generateContent(GeneratedPrimeItem generatedPrimeItem) {
        Mustache newPrimePostTemplate = mustacheEngine.getMustache(getStatus().name());
        return newPrimePostTemplate.render(generatedPrimeItem);
    }

    public abstract PrimePostStatus getStatus();
}