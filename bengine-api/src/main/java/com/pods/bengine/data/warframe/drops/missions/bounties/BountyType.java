package com.pods.bengine.data.warframe.drops.missions.bounties;

import java.util.stream.Stream;

public enum BountyType {

    CETUS_BOUNTY("Cetus Bounty"),
    GHOUL_BOUNTY("Ghoul Bounty"),
    ORB_VALLIS_BOUNTY("Orb Vallis Bounty"),
    PROFIT_TAKER_BOUNTY("PROFIT-TAKER");

    private final String parsingName;

    BountyType(String parsingName) {
        this.parsingName = parsingName;
    }


    public static BountyType findTypeByBountyName(String bountyName) {
        return Stream.of(values())
                .filter(type -> bountyName.contains(type.parsingName))
                .findFirst()
                .orElse(null);
    }
}
