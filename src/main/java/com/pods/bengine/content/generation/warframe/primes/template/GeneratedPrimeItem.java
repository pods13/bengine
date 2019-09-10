package com.pods.bengine.content.generation.warframe.primes.template;

import com.pods.bengine.content.generation.warframe.primes.status.PrimePostStatus;
import com.pods.bengine.content.generation.warframe.primes.template.location.ItemPartFarmingLocation;
import com.pods.bengine.data.warframe.drops.missions.bounties.Tier;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeneratedPrimeItem {

    private String name;
    private String normalizedName;
    private PrimePostStatus status;
    private String[] alongWithPrimeItems;
    private Map<String, Set<String>> itemPartsToAvailableRelics;
    private Map<String, Set<String>> itemPartsToAllRelics;
    private Set<ItemPartFarmingLocation> itemPartFarmingLocations;
    private Map<Tier, String> relicsByBountyTiers;
    private boolean draft;

    public static class Builder {

        private String name;
        private String normalizedName;
        private PrimePostStatus status;
        private String[] alongWithPrimeItems;
        private Map<String, Set<String>> itemPartsToAvailableRelics;
        private Map<String, Set<String>> itemPartsToAllRelics;
        private Set<ItemPartFarmingLocation> itemPartFarmingLocations;
        private Map<Tier, String> relicsByBountyTiers;
        private boolean draft;

        public GeneratedPrimeItem build() {
            return new GeneratedPrimeItem(this);
        }

        public Builder addName(String name) {
            this.name = name;
            return this;
        }

        public Builder addNormalizedName(String normalizedName) {
            this.normalizedName = normalizedName;
            return this;
        }

        public Builder addStatus(PrimePostStatus status) {
            this.status = status;
            return this;
        }

        public Builder addAlongWithPrimeItems(List<String> alongWithPrimeItems) {
            this.alongWithPrimeItems = alongWithPrimeItems.toArray(new String[0]);
            return this;
        }

        public Builder addItemPartsToAvailableRelics(Map<String, Set<String>> itemPartsToAvailableRelics) {
            this.itemPartsToAvailableRelics = itemPartsToAvailableRelics;
            return this;
        }

        public Builder addItemPartsToAllRelics(Map<String, Set<String>> itemPartsToAllRelics) {
            this.itemPartsToAllRelics = itemPartsToAllRelics;
            return this;
        }

        public Builder addItemPartFarmingLocations(Set<ItemPartFarmingLocation> itemPartFarmingLocations) {
            this.itemPartFarmingLocations = itemPartFarmingLocations;
            return this;
        }

        public Builder addRelicsByBountyTiers(Map<Tier, String> relicsByBountyTiers) {
            this.relicsByBountyTiers = relicsByBountyTiers;
            return this;
        }

        public Builder addAsDraft(boolean draft) {
            this.draft = draft;
            return this;
        }
    }

    private GeneratedPrimeItem(Builder builder) {
        this.name = builder.name;
        this.normalizedName = builder.normalizedName;
        this.status = builder.status;
        this.alongWithPrimeItems = builder.alongWithPrimeItems;
        this.itemPartsToAvailableRelics = builder.itemPartsToAvailableRelics;
        this.itemPartFarmingLocations = builder.itemPartFarmingLocations;
        this.itemPartsToAllRelics = builder.itemPartsToAllRelics;
        this.relicsByBountyTiers = builder.relicsByBountyTiers;
        this.draft = builder.draft;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public PrimePostStatus getStatus() {
        return status;
    }

    public void setStatus(PrimePostStatus status) {
        this.status = status;
    }

    public String[] getAlongWithPrimeItems() {
        return alongWithPrimeItems;
    }

    public void setAlongWithPrimeItems(String[] alongWithPrimeItems) {
        this.alongWithPrimeItems = alongWithPrimeItems;
    }

    public Map<String, Set<String>> getItemPartsToAvailableRelics() {
        return itemPartsToAvailableRelics;
    }

    public void setItemPartsToAvailableRelics(Map<String, Set<String>> itemPartsToAvailableRelics) {
        this.itemPartsToAvailableRelics = itemPartsToAvailableRelics;
    }

    public Map<String, Set<String>> getItemPartsToAllRelics() {
        return itemPartsToAllRelics;
    }

    public void setItemPartsToAllRelics(Map<String, Set<String>> itemPartsToAllRelics) {
        this.itemPartsToAllRelics = itemPartsToAllRelics;
    }

    public Set<ItemPartFarmingLocation> getItemPartFarmingLocations() {
        return itemPartFarmingLocations;
    }

    public void setItemPartFarmingLocations(Set<ItemPartFarmingLocation> itemPartFarmingLocations) {
        this.itemPartFarmingLocations = itemPartFarmingLocations;
    }

    public Map<Tier, String> getRelicsByBountyTiers() {
        return relicsByBountyTiers;
    }

    public void setRelicsByBountyTiers(Map<Tier, String> relicsByBountyTiers) {
        this.relicsByBountyTiers = relicsByBountyTiers;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }
}
