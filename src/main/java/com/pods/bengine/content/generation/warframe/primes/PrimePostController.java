package com.pods.bengine.content.generation.warframe.primes;

import com.google.gson.Gson;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/primes")
public class PrimePostController {

    private final JobLauncher jobLauncher;
    private final Job trioPrimePostsGenerationJob;

    public PrimePostController(JobLauncher jobLauncher, Job trioPrimePostsGenerationJob) {
        this.jobLauncher = jobLauncher;
        this.trioPrimePostsGenerationJob = trioPrimePostsGenerationJob;
    }

    @PostMapping
    @ResponseBody
    public void createPrimePost(@RequestParam String primedItems, @RequestParam String status,
                                @RequestParam(defaultValue = "true") boolean draft) {
        //TODO add validation for primedItems data
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("items", primedItems)
                .addString("status", status)
                .addString("draft", Boolean.toString(draft))
                .toJobParameters();

        try {
            jobLauncher.run(trioPrimePostsGenerationJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException
                | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
