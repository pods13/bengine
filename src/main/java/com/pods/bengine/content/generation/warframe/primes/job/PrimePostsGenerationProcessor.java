package com.pods.bengine.content.generation.warframe.primes.job;

import com.github.slugify.Slugify;
import com.pods.bengine.content.generation.warframe.primes.PrimePostData;
import com.pods.bengine.content.generation.warframe.primes.generator.PrimePostGenerator;
import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatusResolver;
import com.pods.bengine.content.generation.warframe.primes.template.GeneratedPrimeItem;
import com.pods.bengine.content.generation.warframe.primes.template.location.ItemPartFarmingLocation;
import com.pods.bengine.content.generation.warframe.primes.template.location.ItemPartFarmingLocationService;
import com.pods.bengine.content.post.warframe.primes.PrimePost;
import com.pods.bengine.data.warframe.drops.missions.bounties.Tier;
import com.pods.bengine.data.warframe.drops.relics.Relic;
import com.pods.bengine.data.warframe.drops.relics.RelicService;
import com.pods.bengine.data.warframe.drops.relics.RelicType;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PrimePostsGenerationProcessor implements ItemProcessor<PrimePostData, PrimePost> {

    private final RelicService relicService;
    private final List<PrimePostGenerator> generators;
    private final ItemPartFarmingLocationService itemPartFarmingLocationService;
    private final Slugify slugify;
    private final PrimePostStatusResolver postStatusResolver;

    public PrimePostsGenerationProcessor(RelicService relicService, List<PrimePostGenerator> generators,
                                         ItemPartFarmingLocationService itemPartFarmingLocationService, Slugify slugify,
                                         PrimePostStatusResolver postStatusResolver) {
        this.relicService = relicService;
        this.generators = generators;
        this.itemPartFarmingLocationService = itemPartFarmingLocationService;
        this.slugify = slugify;
        this.postStatusResolver = postStatusResolver;
    }

    @Override
    public PrimePost process(@NotNull PrimePostData postData) {
        return Stream.of(postData)
                .filter(data -> Objects.nonNull(data.getItemName()))
                .filter(this::isStatusChanged)
                .map(this::toGeneratedPrimeItem)
                .map(this::generatePrimePost)
                .findFirst()
                .orElse(null);
    }

    private boolean isStatusChanged(PrimePostData postData) {
        Map<String, Set<String>> primeItemNameToAvailableRelics = retrievePrimeItemNameToAvailableRelics();
        PrimePostStatus newStatus = postStatusResolver.resolve(postData, primeItemNameToAvailableRelics);
        return newStatus != postData.getStatus();
    }

    private GeneratedPrimeItem toGeneratedPrimeItem(PrimePostData postData) {
        Map<String, Set<String>> primeItemNameToAvailableRelics = retrievePrimeItemNameToAvailableRelics();
        Set<String> allRelicNames = relicService.getAllRelics().stream()
                .filter(relic -> RelicType.INTACT.equals(relic.getType()))
                .map(Relic::getName)
                .collect(Collectors.toSet());
        Map<String, Set<String>> itemPartsToAllRelics = relicService.getItemPartsToRelics(postData.getItemName(), allRelicNames);
        Set<String> availableRelicNames = primeItemNameToAvailableRelics.get(postData.getItemName());
        Map<String, Set<String>> itemPartsToAvailableRelics = relicService.getItemPartsToRelics(postData.getItemName(), availableRelicNames);
        Set<ItemPartFarmingLocation> itemPartFarmingLocations =
                itemPartFarmingLocationService.resolveItemPartFarmingLocations(itemPartsToAvailableRelics);
        Set<String> relicNames = itemPartsToAvailableRelics.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Map<Tier, String> relicRewardByBountyTiers = relicService.sortRelicNamesByBountyTiers(relicNames);
        return new GeneratedPrimeItem.Builder()
                .addName(postData.getItemName())
                .addNormalizedName(slugify.slugify(postData.getItemName()))
                .addStatus(postStatusResolver.resolve(postData, primeItemNameToAvailableRelics))
                .addAlongWithPrimeItems(postData.getAlongWithPrimeItems())
                .addItemPartsToAvailableRelics(itemPartsToAvailableRelics)
                .addItemPartsToAllRelics(itemPartsToAllRelics)
                .addItemPartFarmingLocations(itemPartFarmingLocations)
                .addRelicsByBountyTiers(relicRewardByBountyTiers)
                .addAsDraft(postData.isDraft())
                .build();
    }

    private Map<String, Set<String>> retrievePrimeItemNameToAvailableRelics() {
        Set<Relic> intactRelics = relicService.getAvailableRelics().stream()
                .filter(relic -> RelicType.INTACT.equals(relic.getType()))
                .collect(Collectors.toSet());
        Map<String, Set<String>> primeItemNameToAvailableRelics =
                relicService.getPrimeItemNamesByAllRelics().stream()
                        .collect(Collectors.toMap(Function.identity(), v -> new HashSet<>()));

        for (Relic relic : intactRelics) {
            for (String primeItem : relicService.getPrimeItemNamesByRelic(relic)) {
                primeItemNameToAvailableRelics.computeIfAbsent(primeItem, m -> new HashSet<>())
                        .add(relic.getName());
            }
        }
        return primeItemNameToAvailableRelics;
    }

    private PrimePost generatePrimePost(GeneratedPrimeItem generatedPrimeItem) {
        PrimePostGenerator generator = generators.stream()
                .filter(g -> g.getStatus().equals(generatedPrimeItem.getStatus()))
                .findFirst()
                .orElseThrow(() -> {
                    throw new RuntimeException("Cannot find generator by status");
                });
        return generator.generate(generatedPrimeItem);
    }
}
