package com.pods.bengine.data.warframe.drops.relics;

import com.pods.bengine.data.warframe.drops.WarframeDropsPageData;
import com.pods.bengine.data.warframe.drops.missions.Mission;
import com.pods.bengine.data.warframe.drops.missions.bounties.Bounty;
import com.pods.bengine.data.warframe.drops.missions.bounties.BountyType;
import com.pods.bengine.data.warframe.drops.missions.bounties.Tier;
import com.pods.bengine.data.warframe.drops.rewards.Reward;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Service
public class RelicService {

    private static final String PRIME_ITEM_INDICATOR = "Prime";

    private final WarframeDropsPageData dropsPageData;

    public RelicService(WarframeDropsPageData dropsPageData) {
        this.dropsPageData = dropsPageData;
    }

    public Set<Relic> getAvailableRelics() {
        Set<Reward> rewards = dropsPageData.getMissions().stream()
                .map(this::getRewardsByMission)
                .flatMap(Collection::stream)
                .collect(toSet());

        Set<String> rewardNames = rewards.stream()
                .map(Reward::getName)
                .collect(toSet());

        return dropsPageData.getRelics().stream()
                .filter(relic -> rewardNames.contains(relic.getName()))
                .collect(toSet());
    }

    private Set<Reward> getRewardsByMission(Mission mission) {
        return mission.getRewardsByRotation().values().stream()
                .flatMap(Collection::stream)
                .collect(toSet());
    }

    public Set<Relic> getAllRelics() {
        return dropsPageData.getRelics();
    }

    public Set<String> getPrimeItemNamesByRelic(Relic relic) {
        return relic.getRewards().stream()
                .filter(reward -> reward.getName().contains(PRIME_ITEM_INDICATOR))
                .map(reward -> {
                    int index = reward.getName().lastIndexOf(PRIME_ITEM_INDICATOR);
                    return reward.getName().substring(0, index + PRIME_ITEM_INDICATOR.length());
                })
                .collect(toSet());
    }

    public Set<String> getPrimeItemNamesByAllRelics() {
        return dropsPageData.getRelics().stream()
                .filter(relic -> RelicType.INTACT.equals(relic.getType()))
                .map(this::getPrimeItemNamesByRelic)
                .flatMap(Collection::stream)
                .collect(toSet());
    }

    public Map<String, Set<String>> getItemPartsToRelics(String primeItem, Set<String> relicNames) {
        Function<Relic, Stream<? extends AbstractMap.SimpleEntry<String, String>>> mapRewardToRelic =
                relic -> relic.getRewards().stream()
                        .filter(reward -> reward.getName().contains(primeItem))
                        .map(reward -> new AbstractMap.SimpleEntry<>(reward.getName(), relic.getName()));

        return dropsPageData.getRelics().stream()
                .filter(relic -> relicNames.contains(relic.getName()))
                .flatMap(mapRewardToRelic)
                .collect(groupingBy(AbstractMap.SimpleEntry::getKey, mapping(AbstractMap.SimpleEntry::getValue, toSet())));

    }

    public Map<Tier, String> sortRelicNamesByBountyTiers(Set<String> relicNames) {
        LinkedHashMap<Tier, Set<String>> tiersToRelicNames = dropsPageData.getBounties().stream()
                .filter(this::isCetusOrOrbVallisBounty)
                .sorted(Comparator.comparing(Bounty::getTier))
                .collect(groupingBy(Bounty::getTier, LinkedHashMap::new, mapping(b -> findRelicReward(b, relicNames),
                        filtering(r -> !r.isEmpty(), Collectors.toSet()))));
        return tiersToRelicNames.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .collect(toMap(Map.Entry::getKey, map -> String.join(",", map.getValue())));
    }

    private boolean isCetusOrOrbVallisBounty(Bounty bounty) {
        BountyType type = bounty.getType();
        return BountyType.CETUS_BOUNTY.equals(type)
                || BountyType.ORB_VALLIS_BOUNTY.equals(type);
    }

    private String findRelicReward(Bounty bounty, Set<String> relicNames) {
        return bounty.getStages().stream()
                .map(this::getRewardsByMission)
                .flatMap(Collection::stream)
                .filter(reward -> relicNames.contains(reward.getName()))
                .map(Reward::getName)
                .findFirst()
                .orElse("");
    }
}
