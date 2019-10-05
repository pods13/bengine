package com.pods.bengine.content.generation.warframe.primes.status;

import com.pods.bengine.content.generation.warframe.primes.PrimePostData;
import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class PrimePostStatusResolver {

    public PrimePostStatus resolve(PrimePostData postData,
                                   Map<String, Set<String>> primeItemNameToAvailableRelics) {
        PrimePostStatus previousStatus = postData.getStatus();
        int numberOfAvailableRelics = primeItemNameToAvailableRelics.get(postData.getItemName()).size();
        if (numberOfAvailableRelics == 0) {
            return PrimePostStatus.VAULTED;
        } else if (PrimePostStatus.VAULTED.equals(previousStatus)) {
            return PrimePostStatus.UNVAULTED;
        }

        return Optional.ofNullable(previousStatus).orElse(PrimePostStatus.NEW);
    }
}
