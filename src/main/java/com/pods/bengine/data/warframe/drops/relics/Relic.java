package com.pods.bengine.data.warframe.drops.relics;

import com.pods.bengine.data.warframe.drops.rewards.Reward;

import java.util.HashSet;
import java.util.Set;

public class Relic {

    private String name;
    private RelicType type;
    private RelicEra relicEra;
    private Set<Reward> rewards;

    public Relic(String name, RelicType type, RelicEra relicEra) {
        this.name = name;
        this.type = type;
        this.relicEra = relicEra;
        this.rewards = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RelicType getType() {
        return type;
    }

    public void setType(RelicType type) {
        this.type = type;
    }

    public RelicEra getRelicEra() {
        return relicEra;
    }

    public void setRelicEra(RelicEra relicEra) {
        this.relicEra = relicEra;
    }

    public Set<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(Set<Reward> rewards) {
        this.rewards = rewards;
    }
}
