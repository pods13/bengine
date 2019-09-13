package com.pods.bengine.content.generation.warframe.primes.job;

import com.pods.bengine.content.generation.warframe.primes.PrimePostData;
import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@StepScope
public class PrimePostsViaJobParametersReader implements ItemReader<PrimePostData> {

    @Value("#{jobParameters['items']}")
    private String items;

    @Value("#{jobParameters['status']}")
    private String status;

    @Value("#{jobParameters['draft']}")
    private String draft;

    private List<PrimePostData> primePostData;

    public PrimePostsViaJobParametersReader() {
        this.primePostData = new ArrayList<>();
    }

    @BeforeStep
    public void createPostDataByParameters() {
        String[] primedItems = items.split(",");
        for (String primedItem : primedItems) {
            PrimePostData data = new PrimePostData();
            data.setItemName(primedItem);
            data.setStatus(PrimePostStatus.valueOf(status));
            data.setDraft(Boolean.parseBoolean(draft));
            List<String> alongWithItems = Stream.of(primedItems)
                    .filter(item -> !item.equals(primedItem))
                    .collect(Collectors.toList());
            data.setAlongWithPrimeItems(alongWithItems);

            primePostData.add(data);
        }
    }


    @Override
    public PrimePostData read() {
        if (!primePostData.isEmpty()) {
            return primePostData.remove(0);
        }
        return null;
    }
}
