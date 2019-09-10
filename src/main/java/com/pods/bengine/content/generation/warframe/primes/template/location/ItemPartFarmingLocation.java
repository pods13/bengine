package com.pods.bengine.content.generation.warframe.primes.template.location;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class ItemPartFarmingLocation {

    private Set<String> itemPartNames;
    private Set<FarmingLocation> farmingLocations;

    public ItemPartFarmingLocation() {
        this.itemPartNames = new HashSet<>();
        this.farmingLocations = new LinkedHashSet<>();
    }

    public Set<String> getItemPartNames() {
        return itemPartNames;
    }

    public void setItemPartNames(Set<String> itemPartNames) {
        this.itemPartNames = itemPartNames;
    }

    public Set<FarmingLocation> getFarmingLocations() {
        return farmingLocations;
    }

    public void setFarmingLocations(Set<FarmingLocation> farmingLocations) {
        this.farmingLocations = farmingLocations;
    }
}
