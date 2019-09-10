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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/primes")
public class PrimePostController {

    private final JobLauncher jobLauncher;
    private final Job singlePrimePostGenerationJob;

    public PrimePostController(JobLauncher jobLauncher, Job singlePrimePostGenerationJob) {
        this.jobLauncher = jobLauncher;
        this.singlePrimePostGenerationJob = singlePrimePostGenerationJob;
    }

    @PostMapping
    @ResponseBody
    public void createPrimePost(@RequestBody PrimePostData postData) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("postData", new Gson().toJson(postData))
                .toJobParameters();

        try {
            jobLauncher.run(singlePrimePostGenerationJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException
                | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException();
        }
    }
}
