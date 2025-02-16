package com.tfkfan.vertx.math;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Vector implements Serializable {
    private double x = 0.0;
    private double y = 0.0;

    public static final double RAD_TO_DEG = 180 / Math.PI;
    public static final Vector REFERENCE = new Vector(1.0, 0.0);

    public static Vector diff(Vector v1, Vector v2) {
        return new Vector(v2.x - v1.x, v2.y - v1.y);
    }

    public static double getDistance(Vector vector1, Vector vector2) {
        return Math.sqrt(Math.pow((vector2.x - vector1.x), 2.0) + Math.pow((vector2.y - vector1.y), 2.0));
    }

    public static double getLength(Vector vector) {
        return Math.sqrt(Math.pow(vector.x, 2.0) + Math.pow(vector.y, 2.0));
    }

    public static Vector normalize(Vector v1) {
        var length = getLength(v1);
        if (length != 0.0) return new Vector(v1.x / length, v1.y / length);

        return new Vector(0.0, 0.0);
    }

    public static Vector multiply(Vector vector, double scalar) {
        return new Vector(vector.x * scalar, vector.y * scalar);
    }

    public static Vector divide(Vector vector, double scalar) {
        return new Vector(vector.x / scalar, vector.y / scalar);
    }

    public static double scalar(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static double angleRadians(Vector v1, Vector v2) {
        return Math.acos(scalar(v1, v2) / (getLength(v1) * getLength(v2)));
    }

    public static double angleDegrees(Vector v1, Vector v2) {
        return RAD_TO_DEG * angleRadians(v1, v2);
    }

    public Vector() {
        y = 0.0;
        x = 0.0;
    }

    public Vector(double x, double y) {
        set(x, y);
    }

    public Vector(Vector vector) {
        set(vector);
    }

    public Vector set(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        return this;
    }

    public Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector sumX(double x) {
        this.x += x;
        return this;
    }

    public Vector sumY(double y) {
        this.y += y;
        return this;
    }

    public Vector sum(Vector vector) {
        sumX(vector.x);
        sumY(vector.y);
        return this;
    }

    public Vector sumNew(Vector vector) {
        var v = new Vector(this);
        v.sumX(vector.x);
        v.sumY(vector.y);
        return v;
    }

    public Vector sum(double x, double y) {
        sumX(x);
        sumY(y);
        return this;
    }

    public Vector sumNew(double x, double y) {
        var v = new Vector(this);
        v.sumX(x);
        v.sumY(y);
        return v;
    }

    public Vector multiplyX(double x) {
        this.x *= x;
        return this;
    }

    public Vector multiplyY(double y) {
        this.y *= y;
        return this;
    }

    public Vector multiply(Vector vector) {
        multiplyX(vector.x);
        multiplyY(vector.y);
        return this;
    }

    public Vector multiply(double x, double y) {
        multiplyX(x);
        multiplyY(y);
        return this;
    }

    public Vector divideX(double x) {
        this.x /= x;
        return this;
    }

    public Vector divideY(double y) {
        this.y /= y;
        return this;
    }

    public Vector divide(Vector vector) {
        divideX(vector.x);
        divideY(vector.y);
        return this;
    }

    public Vector divide(double x, double y) {
        divideX(x);
        divideY(y);
        return this;
    }

    public Vector diff(Vector vector) {
        diff(vector.x, vector.y);
        return this;
    }

    public Vector diff(double x, double y) {
        diffX(x);
        diffY(y);
        return this;
    }

    public Vector diffX(double x) {
        this.x -= x;
        return this;
    }

    public Vector diffY(double y) {
        this.y -= y;
        return this;
    }

    public Vector reduce(double value) {
        var valueNew = Math.abs(value);
        if (x > 0) diffX(valueNew);
        else if (x < 0) sumX(valueNew);

        if (y > 0) diffY(valueNew);
        else if (y < 0) sumY(valueNew);
        return this;
    }

    public Vector inverse() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public Vector normalize() {
        set(normalize(this));
        return this;
    }

    public Vector normalizeNew() {
        return normalize(this);
    }

    public Vector inverseNew() {
        return new Vector(-x, -y);
    }

    public void inverse(double multiplicator) {
        x = (-multiplicator * x);
        y = (-multiplicator * y);
    }
}
