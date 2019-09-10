package com.pods.bengine.content.generation.warframe.primes.template.location;

import com.pods.bengine.content.generation.warframe.primes.template.location.mission.MissionTemplateResolver;
import com.pods.bengine.data.warframe.drops.missions.Mission;
import com.pods.bengine.data.warframe.drops.missions.MissionService;
import com.pods.bengine.data.warframe.drops.relics.RelicEra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Service
public class ItemPartFarmingLocationService {

    private final MissionTemplateResolver missionResolver;

    @Autowired
    MissionService missionService;

    public ItemPartFarmingLocationService(MissionTemplateResolver missionResolver) {
        this.missionResolver = missionResolver;
    }

    public Set<ItemPartFarmingLocation> resolveItemPartFarmingLocations(Map<String, Set<String>> itemPartsToRelics) {
        Set<ItemPartFarmingLocation> itemPartFarmingLocations = new LinkedHashSet<>();
        Set<String> sortedItemPartNames = new HashSet<>();
        Map<String, Set<String>> itemPartsMentionedInMissions = new HashMap<>();
        for (Map.Entry<String, Set<String>> currentItemPartToRelics : itemPartsToRelics.entrySet()) {
            ItemPartFarmingLocation itemPartFarmingLocation = new ItemPartFarmingLocation();

            String currentItemPart = currentItemPartToRelics.getKey();
            if (sortedItemPartNames.contains(currentItemPart)) {
                continue;
            }
            itemPartFarmingLocation.getItemPartNames().add(currentItemPart);
            sortedItemPartNames.add(currentItemPart);

            Set<String> currentRelicNames = currentItemPartToRelics.getValue();
            Set<String> allRelicNames = new HashSet<>(currentRelicNames);
            Set<RelicEra> currentRelicEras = retrieveRelicEras(currentRelicNames);
            for (Map.Entry<String, Set<String>> itemPartToRelics : itemPartsToRelics.entrySet()) {
                String itemPart = itemPartToRelics.getKey();
                if (sortedItemPartNames.contains(itemPart)) {
                    continue;
                }

                Set<String> relicNames = itemPartToRelics.getValue();
                Set<RelicEra> relicEras = retrieveRelicEras(relicNames);
                if (currentRelicEras.equals(relicEras)) {
                    itemPartFarmingLocation.getItemPartNames().add(itemPart);
                    sortedItemPartNames.add(itemPart);
                    allRelicNames.addAll(relicNames);
                }
            }

            Map<RelicEra, Set<String>> relicErasToNames = collectRelicErasToRelicNames(allRelicNames);
            for (Map.Entry<RelicEra, Set<String>> relicEraToNames : relicErasToNames.entrySet()) {
                RelicEra era = relicEraToNames.getKey();
                Set<String> relicNames = relicEraToNames.getValue();
                Mission mission = missionResolver.resolve(era, relicNames);
                //TODO log error or throw it when mission is null?
                if (itemPartsMentionedInMissions.containsKey(mission.getName())) {
                    itemPartFarmingLocation.getFarmingLocations()
                            .add(new FarmingLocation(mission, era, relicNames, itemPartsMentionedInMissions.get(mission.getName())));
                } else {
                    itemPartFarmingLocation.getFarmingLocations()
                            .add(new FarmingLocation(mission, era, relicNames));
                }
                itemPartsMentionedInMissions.put(mission.getName(), itemPartFarmingLocation.getItemPartNames());
            }
            itemPartFarmingLocations.add(itemPartFarmingLocation);
        }


        return itemPartFarmingLocations;
    }

    private Set<RelicEra> retrieveRelicEras(Set<String> relicNames) {
        return relicNames.stream()
                .map(RelicEra::findRelicEraByFullRelicName)
                .collect(Collectors.toSet());
    }

    private Map<RelicEra, Set<String>> collectRelicErasToRelicNames(Set<String> relicNames) {
        return relicNames.stream()
                .collect(Collectors.groupingBy(RelicEra::findRelicEraByFullRelicName,
                        mapping(Function.identity(), toSet())));
    }
}
