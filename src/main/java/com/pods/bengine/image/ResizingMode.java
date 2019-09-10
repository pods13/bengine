package com.pods.bengine.image;

public enum ResizingMode {
    CONTENT(ImageSize.LARGE, ImageSize.MEDUIM, ImageSize.SMALL),
    FEATURE(ImageSize.LARGE, ImageSize.THUMBNAIL);

    private final ImageSize[] sizes;

    ResizingMode(ImageSize... sizes) {
        this.sizes = sizes;
    }

    public ImageSize[] getSizes() {
        return sizes;
    }
}
