package com.pods.bengine.data.warframe.drops.relics;

import java.util.stream.Stream;

public enum RelicEra {
    LITH,
    MESO,
    NEO,
    AXI;

    public static RelicEra findRelicEraByFullRelicName(String fullRelicName) {
        return Stream.of(values())
                .filter(relicEra -> fullRelicName.contains(relicEra.toString()))
                .findFirst()
                .orElse(null);
    }


    @Override
    public String toString() {
        return name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
    }
}
