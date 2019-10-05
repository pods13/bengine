package com.pods.bengine.image;

import com.tinify.Options;

public enum ImageSize {
    THUMBNAIL(360, 240, "thumb"),
    SMALL(640, null, "scale"),
    MEDUIM(768, null, "scale"),
    LARGE(960, null, "scale");

    private final Integer width;
    private final Integer height;
    private final String method;

    ImageSize(Integer width, Integer height, String method) {
        this.width = width;
        this.height = height;
        this.method = method;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getMethod() {
        return method;
    }
}
