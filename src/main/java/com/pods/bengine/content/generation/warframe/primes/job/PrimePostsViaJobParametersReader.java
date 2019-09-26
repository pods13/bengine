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

    private final String frame;
    private final String gear;
    private final String status;
    private final boolean draft;

    private List<PrimePostData> primePostData;

    public PrimePostsViaJobParametersReader(@Value("#{jobParameters['frame']}") String frame,
                                            @Value("#{jobParameters['gear']}") String gear,
                                            @Value("#{jobParameters['status']}") String status,
                                            @Value("#{jobParameters['draft']}") String draft) {
        this.frame = frame;
        this.gear = gear;
        this.status = status;
        this.draft = Boolean.parseBoolean(draft);
        this.primePostData = new ArrayList<>();
    }

    @BeforeStep
    public void createPostDataByParameters() {
        String items = String.join(",", frame, gear);
        String[] primedItems = items.split(",");
        for (String primedItem : primedItems) {
            PrimePostData data = new PrimePostData();
            data.setGroupId(generateGroupId(frame));
            data.setItemName(primedItem);
            data.setStatus(PrimePostStatus.valueOf(status));
            data.setDraft(draft);
            List<String> alongWithItems = Stream.of(primedItems)
                    .filter(item -> !item.equals(primedItem))
                    .collect(Collectors.toList());
            data.setAlongWithPrimeItems(alongWithItems);

            primePostData.add(data);
        }
    }

    private String generateGroupId(String frame) {
        return String.join(" ", frame.toLowerCase(), "access")
                .replace(" ", "-");
    }


    @Override
    public PrimePostData read() {
        if (!primePostData.isEmpty()) {
            return primePostData.remove(0);
        }
        return null;
    }
}
