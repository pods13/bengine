package com.pods.bengine.image;

public enum ImageFormats {
    PNG,
    JPG,
    JPEG;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public String asFileExtension() {
        return "." + toString();
    }
}
