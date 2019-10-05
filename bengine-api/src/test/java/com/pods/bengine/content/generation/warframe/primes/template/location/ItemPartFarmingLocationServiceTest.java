package com.pods.bengine.content.generation.warframe.primes.template.location;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ItemPartFarmingLocationServiceTest {

    private ItemPartFarmingLocationService itemPartFarmingLocationService =
            new ItemPartFarmingLocationService(null);

    @Test
    public void testItemPartsHaveSameRelicEra() {
        final String rhinoPrimeBlueprint = "Rhino Prime Blueprint";
        final String rhinoPrimeSystem = "Rhino Prime System";
        final Map<String, Set<String>> itemPartToRelicNames = new HashMap<>();
        itemPartToRelicNames.put(rhinoPrimeBlueprint, Stream.of("Neo S7", "Axi S3")
                .collect(toSet()));
        itemPartToRelicNames.put(rhinoPrimeSystem, Stream.of("Axi S7", "Neo S9")
                .collect(toSet()));
        Set<ItemPartFarmingLocation> itemPartFarmingLocations =
                itemPartFarmingLocationService.resolveItemPartFarmingLocations(itemPartToRelicNames);

        ItemPartFarmingLocation itemPartFarmingLocation = itemPartFarmingLocations.iterator().next();
        Assert.assertEquals(Stream.of(rhinoPrimeBlueprint, rhinoPrimeSystem).collect(toSet()),
                itemPartFarmingLocation.getItemPartNames());
    }

    @Test
    public void testItemPartsHaveAtLeastOneDifferentRelicEra() {
        final String rhinoPrimeBlueprint = "Rhino Prime Blueprint";
        final String rhinoPrimeSystem = "Rhino Prime System";
        final Map<String, Set<String>> itemPartToRelicNames = new HashMap<>();
        itemPartToRelicNames.put(rhinoPrimeBlueprint, Stream.of("Lith C7", "Axi S3")
                .collect(toSet()));
        itemPartToRelicNames.put(rhinoPrimeSystem, Stream.of("Axi S7")
                .collect(toSet()));
        Set<ItemPartFarmingLocation> itemPartFarmingLocations =
                itemPartFarmingLocationService.resolveItemPartFarmingLocations(itemPartToRelicNames);

        Assert.assertEquals(2, itemPartFarmingLocations.size());

    }
}
