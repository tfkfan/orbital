package io.github.tfkfan.orbital.core.math.random;

public class Random {
    public static int getRandomIndex(int max) {
        return getRandomNumber(0, max);
    }

    public static int  getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static double  getRandomNumber(double min, double max){
        return ((Math.random() * (max - min)) + min);
    }
}

