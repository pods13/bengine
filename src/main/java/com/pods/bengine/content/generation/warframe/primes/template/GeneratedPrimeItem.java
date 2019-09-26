package com.pods.bengine.content.generation.warframe.primes.template;

import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import com.pods.bengine.content.generation.warframe.primes.template.location.ItemPartFarmingLocation;
import com.pods.bengine.data.warframe.drops.missions.bounties.Tier;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
@Builder
public class GeneratedPrimeItem {

    private final String groupId;
    private final String name;
    private final String normalizedName;
    private final PrimePostStatus status;
    private final String[] alongWithPrimeItems;
    private final Map<String, Set<String>> itemPartsToAvailableRelics;
    private final Map<String, Set<String>> itemPartsToAllRelics;
    private final Set<ItemPartFarmingLocation> itemPartFarmingLocations;
    private final Map<Tier, String> relicsByBountyTiers;
    private final boolean draft;
}
