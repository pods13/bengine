package com.pods.bengine.config.batch;

import com.pods.bengine.content.generation.warframe.primes.PrimePostData;
import com.pods.bengine.content.generation.warframe.primes.job.PrimePostItemReader;
import com.pods.bengine.content.generation.warframe.primes.job.PrimePostListItemReader;
import com.pods.bengine.content.generation.warframe.primes.job.PrimePostListItemWriter;
import com.pods.bengine.content.generation.warframe.primes.job.PrimePostsGenerationProcessor;
import com.pods.bengine.content.post.warframe.primes.PrimePost;
import com.pods.bengine.content.post.warframe.primes.PrimePostFactory;
import com.pods.bengine.content.post.warframe.primes.PrimePostMatter;
import com.pods.bengine.content.reader.PostReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrimePostsGenerationJobConfig {

    private final PrimePostListItemReader primePostListItemReader;
    private final PrimePostsGenerationProcessor primePostsGenerationProcessor;
    private final PrimePostListItemWriter primePostListItemWriter;

    private final PrimePostItemReader primePostItemReader;

    public PrimePostsGenerationJobConfig(PrimePostListItemReader primePostListItemReader,
                                         PrimePostsGenerationProcessor primePostsGenerationProcessor,
                                         PrimePostListItemWriter primePostListItemWriter,
                                         PrimePostItemReader primePostItemReader) {
        this.primePostListItemReader = primePostListItemReader;
        this.primePostsGenerationProcessor = primePostsGenerationProcessor;
        this.primePostListItemWriter = primePostListItemWriter;
        this.primePostItemReader = primePostItemReader;
    }

    @Bean
    public Job primePostsGenerationJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        return jobBuilderFactory.get("primePostsGenerationJob")
                .start(generatePrimePostsStep(stepBuilderFactory))
                .build();
    }

    @Bean
    public Step generatePrimePostsStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("generatePrimePostsStep")
                .<PrimePostData, PrimePost>chunk(5)
                .reader(primePostListItemReader)
                .processor(primePostsGenerationProcessor)
                .writer(primePostListItemWriter)
                .build();
    }

    @Bean
    public Job singlePrimePostGenerationJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        return jobBuilderFactory.get("singlePrimePostGenerationJob")
                .start(generateSinglePrimePostStep(stepBuilderFactory))
                .build();
    }

    @Bean
    public Step generateSinglePrimePostStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("generateSinglePrimePostStep")
                .<PrimePostData, PrimePost>chunk(1)
                .reader(primePostItemReader)
                .processor(primePostsGenerationProcessor)
                .writer(primePostListItemWriter)
                .build();
    }

    @Bean
    public PrimePostFactory primePostFactory() {
        return new PrimePostFactory();
    }

    @Bean
    public PostReader<PrimePost> primePostReader() {
        return new PostReader<>(primePostFactory(), PrimePostMatter.class);
    }
}
