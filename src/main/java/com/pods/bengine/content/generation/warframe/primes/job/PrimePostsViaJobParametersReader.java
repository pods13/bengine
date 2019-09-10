package com.pods.bengine.content.generation.warframe.primes.job;

import com.google.gson.Gson;
import com.pods.bengine.content.generation.warframe.primes.PrimePostData;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PrimePostsViaParametersReader implements ItemReader<PrimePostData> {

    @Value("#{jobParameters['postData']}")
    private String data;

    private PrimePostData postData;

    @BeforeStep
    public void getPost() {
        postData = new Gson().fromJson(data, PrimePostData.class);
    }

    @Override
    public PrimePostData read() {
        PrimePostData next = postData;
        postData = null;
        return next;
    }
}
