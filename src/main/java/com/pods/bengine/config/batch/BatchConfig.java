package com.pods.bengine.config.batch;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@Import({PrimePostsGenerationJobConfig.class})
public class BatchConfig extends DefaultBatchConfigurer {
}
