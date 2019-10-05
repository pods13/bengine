package com.pods.bengine.config;

import com.github.slugify.Slugify;
import com.pods.bengine.config.batch.BatchConfig;
import com.pods.bengine.config.cache.CacheConfig;
import com.pods.bengine.config.security.SecurityConfig;
import com.pods.bengine.content.generation.warframe.primes.template.helpers.NumberOfRelicsHelper;
import com.pods.bengine.content.post.warframe.primes.PrimePost;
import com.pods.bengine.content.post.warframe.primes.PrimePostMatter;
import com.pods.bengine.content.writer.PostWriter;
import com.pods.bengine.core.minify.HtmlMinifier;
import com.pods.bengine.data.warframe.drops.missions.bounties.Tier;
import org.kohsuke.github.GitHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.locator.ClassPathTemplateLocator;
import org.trimou.handlebars.Helper;
import org.trimou.handlebars.HelpersBuilder;
import org.trimou.lambda.SimpleLambdas;
import org.trimou.minify.Minify;

import java.io.IOException;
import java.util.Map;

@Configuration
@EnableScheduling
@Import({BatchConfig.class, SecurityConfig.class, CacheConfig.class})
public class ApplicationConfig {

    private final Environment env;

    public ApplicationConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public PostWriter<PrimePost> primePostWriter() {
        return new PostWriter<>(PrimePostMatter.class);
    }

    @Bean
    public HtmlMinifier htmlMinifier() {
        return new HtmlMinifier();
    }

    @Bean
    public MustacheEngine mustacheEngine() {
        Map<String, Helper> helpers = HelpersBuilder.all()
                .build();
        return MustacheEngineBuilder.newBuilder()
                .addTemplateLocator(new ClassPathTemplateLocator(0, "warframe/primes", "mustache"))
                .addMustacheListener(Minify.customListener(htmlMinifier()))
                .registerHelpers(helpers)
                .registerHelper("numberOfRelics", new NumberOfRelicsHelper())
                .addGlobalData("toPrimeItemLink", SimpleLambdas.invoke((primeItemName) -> {
                    Slugify slugify = slugify();
                    String href = String.format("/primes/how-to-get-%s/", slugify.slugify(primeItemName));
                    String title = String.format("How To Get %s", primeItemName);
                    return String.format("<a href=\"%s\" title=\"%s\">%s</a>", href, title, primeItemName);
                }))
                .addGlobalData("tier", Tier.class)
                .build();
    }

    @Bean
    public Slugify slugify() {
        return new Slugify();
    }

    @Bean
    public GitHub githubClient() throws IOException {
        return GitHub.connectUsingOAuth(env.getProperty("GITHUB_TOKEN"));
    }
}
