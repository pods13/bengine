package com.pods.bengine.content.generation.warframe.primes.status.resolver;

import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatusResolver;
import com.pods.bengine.content.post.warframe.primes.PrimePost;
import com.pods.bengine.content.post.warframe.primes.PrimePostMatter;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrimePostStatusResolverTest {

    private PrimePostStatusResolver postStatusResolver = new PrimePostStatusResolver();

    @Test
    public void vaultedToUnvaultedStatusTransitionTest() {
        final String primeItemName = "Rhino Prime";
        PrimePostMatter matter = new PrimePostMatter();
        matter.setItemName(primeItemName);
        matter.setStatus(PrimePostStatus.VAULTED);
        PrimePost primePost = new PrimePost(null, matter, null);
        Map<String, Set<String>> primeItemNameToAvailableRelics = Map.of(primeItemName, Set.of("Axi R3", "Neo S7"));
//        PrimePostStatus status = postStatusResolver.resolve(primeItemName, Set.of(primePost), primeItemNameToAvailableRelics);

//        Assert.assertEquals(PrimePostStatus.UNVAULTED, status);
    }

    @Test
    public void unvaultedToVaultedStatusTransitionTest() {
        final String primeItemName = "Rhino Prime";
        PrimePostMatter matter = new PrimePostMatter();
        matter.setItemName(primeItemName);
        matter.setStatus(PrimePostStatus.UNVAULTED);
        PrimePost primePost = new PrimePost(null, matter, null);
        Map<String, Set<String>> primeItemNameToAvailableRelics = Map.of(primeItemName, new HashSet<>());
//        PrimePostStatus status = postStatusResolver.resolve(primeItemName, Set.of(primePost), primeItemNameToAvailableRelics);

//        Assert.assertEquals(PrimePostStatus.VAULTED, status);
    }

    @Test
    public void newToVaultedStatusTransitionTest() {
        final String primeItemName = "Rhino Prime";
        PrimePostMatter matter = new PrimePostMatter();
        matter.setItemName(primeItemName);
        matter.setStatus(PrimePostStatus.NEW);
        PrimePost primePost = new PrimePost(null, matter, null);
        Map<String, Set<String>> primeItemNameToAvailableRelics = Map.of(primeItemName, new HashSet<>());
//        PrimePostStatus status = postStatusResolver.resolve(primeItemName, Set.of(primePost), primeItemNameToAvailableRelics);

//        Assert.assertEquals(PrimePostStatus.VAULTED, status);
    }
}
