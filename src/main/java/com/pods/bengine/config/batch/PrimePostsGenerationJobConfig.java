package com.pods.bengine.config.batch;

import com.pods.bengine.content.generation.warframe.primes.PrimePostData;
import com.pods.bengine.content.generation.warframe.primes.job.PrimePostsViaJobParametersReader;
import com.pods.bengine.content.generation.warframe.primes.job.PrimePostsOnGithubReader;
import com.pods.bengine.content.generation.warframe.primes.job.PrimePostsWriter;
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

    private final PrimePostsOnGithubReader primePostsOnGithubReader;
    private final PrimePostsGenerationProcessor primePostsGenerationProcessor;
    private final PrimePostsWriter primePostsWriter;

    private final PrimePostsViaJobParametersReader primePostsViaJobParametersReader;

    public PrimePostsGenerationJobConfig(PrimePostsOnGithubReader primePostsOnGithubReader,
                                         PrimePostsGenerationProcessor primePostsGenerationProcessor,
                                         PrimePostsWriter primePostsWriter,
                                         PrimePostsViaJobParametersReader primePostsViaJobParametersReader) {
        this.primePostsOnGithubReader = primePostsOnGithubReader;
        this.primePostsGenerationProcessor = primePostsGenerationProcessor;
        this.primePostsWriter = primePostsWriter;
        this.primePostsViaJobParametersReader = primePostsViaJobParametersReader;
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
                .reader(primePostsOnGithubReader)
                .processor(primePostsGenerationProcessor)
                .writer(primePostsWriter)
                .build();
    }

    @Bean
    public Job trioPrimePostsGenerationJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        return jobBuilderFactory.get("trioPrimePostsGenerationJob")
                .start(generateTrioPrimePostsStep(stepBuilderFactory))
                .build();
    }

    @Bean
    public Step generateTrioPrimePostsStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("generateTrioPrimePostsStep")
                .<PrimePostData, PrimePost>chunk(3)
                .reader(primePostsViaJobParametersReader)
                .processor(primePostsGenerationProcessor)
                .writer(primePostsWriter)
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
