package com.pods.bengine.data.warframe.drops.relics;

import java.util.stream.Stream;

public enum RelicType {
    INTACT,
    EXCEPTIONAL,
    FLAWLESS,
    RADIANT;

    public static RelicType findRelicTypeByFullRelicName(String fullRelicName) {
        return Stream.of(values())
                .filter(relicType -> fullRelicName.contains(relicType.toString()))
                .findFirst()
                .orElse(null);

    }

    @Override
    public String toString() {
        return name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
    }
}
