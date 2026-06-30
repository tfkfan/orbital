package io.github.tfkfan.orbital.core.math.random;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.math.Vector;

public class Random {
    public static int getRandomIndex(int max) {
        return getRandomNumber(0, max);
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static Vector2D getRandomVector2D(int min, int max) {
        return new Vector2D(getRandomNumber(min, max), getRandomNumber(min, max));
    }

    public static Vector getRandomVector3D(int min, int max) {
        return new Vector(getRandomNumber(min, max), getRandomNumber(min, max), getRandomNumber(min, max));
    }

    public static double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }
}

