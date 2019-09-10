package com.pods.bengine.data.warframe.drops.missions.bounties;

import java.util.stream.Stream;

public enum Tier {

    ONE("5 - 15"),
    TWO("10 - 30"),
    THREE("20 - 40"),
    FOUR("30 - 50"),
    FIVE("40 - 60");

    private final String levels;

    Tier(String levels) {
        this.levels = levels;
    }

    public static Tier findTierByEnemyLevels(String bountyName) {
        return Stream.of(values())
                .filter(tier -> bountyName.contains(tier.levels))
                .findFirst()
                .orElse(null);
    }


    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
