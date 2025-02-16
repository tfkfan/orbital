package com.tfkfan.vertx.math;

import java.lang.reflect.Type;

public class Utils {
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

