package com.pods.bengine.data.warframe.drops.missions.bounties;

import com.pods.bengine.data.warframe.drops.missions.Mission;

import java.util.HashSet;
import java.util.Set;

public class Bounty {

    private String name;
    private Tier tier;
    private BountyType type;
    private Set<Mission> stages;

    public Bounty(String name, Tier tier, BountyType type) {
        this.name = name;
        this.tier = tier;
        this.type = type;
        this.stages = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public BountyType getType() {
        return type;
    }

    public void setType(BountyType type) {
        this.type = type;
    }

    public Set<Mission> getStages() {
        return stages;
    }

    public void setStages(Set<Mission> stages) {
        this.stages = stages;
    }
}
