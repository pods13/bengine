package com.pods.bengine.data.warframe.drops;

import java.util.stream.Stream;

public enum Rotation {

    A("Rotation A"),
    B("Rotation B"),
    C("Rotation C"),

    NO_ROTATION("No Rotation");

    private final String rotationName;

    Rotation(String rotationName) {
        this.rotationName = rotationName;
    }

    public String getRotationName() {
        return rotationName;
    }

    public static Rotation findRotationByName(String rotationName) {
        return Stream.of(values())
                .filter(rotation -> rotation.getRotationName().equals(rotationName))
                .findFirst()
                .orElse(null);

    }
}
