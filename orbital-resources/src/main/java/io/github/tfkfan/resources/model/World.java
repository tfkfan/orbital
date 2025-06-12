package io.github.tfkfan.resources.model;

public class World {
    Integer width;
    Integer height;

    public World() {
    }

    public World(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public World setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public World setHeight(Integer height) {
        this.height = height;
        return this;
    }
}