package org.project.cryptopal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Logo {
    @JsonProperty("uri")
    private String uri;

    @JsonProperty("height")
    private int height;

    @JsonProperty("width")
    private int width;

    // Getters and Setters
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
