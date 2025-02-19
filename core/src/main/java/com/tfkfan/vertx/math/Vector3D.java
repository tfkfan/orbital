package com.tfkfan.vertx.math;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Vector3D {
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    public static final double RAD_TO_DEG = 180 / Math.PI;
    public static final Vector3D REFERENCE = new Vector3D(1.0, 0.0, 0.0);

    public static Vector3D diff(Vector3D v1, Vector3D v2) {
        return new Vector3D(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }

    public static double getDistance(Vector3D vector1, Vector3D vector2) {
        return Math.sqrt(Math.pow((vector2.x - vector1.x), 2.0) + Math.pow((vector2.y - vector1.y), 2.0) + Math.pow((vector2.z - vector1.z), 2.0));
    }

    public static double getLength(Vector3D vector) {
        return Math.sqrt(Math.pow(vector.x, 2.0) + Math.pow(vector.y, 2.0) + Math.pow(vector.z, 2.0));
    }

    public static Vector3D normalize(Vector3D v1) {
        var length = getLength(v1);
        if (length != 0.0) return new Vector3D(v1.x / length, v1.y / length, v1.z / length);

        return new Vector3D(0.0, 0.0, 0.0);
    }

    public static Vector3D multiply(Vector3D vector, double scalar) {
        return new Vector3D(vector.x * scalar, vector.y * scalar, vector.z * scalar);
    }

    public static Vector3D divide(Vector3D vector, double scalar) {
        return new Vector3D(vector.x / scalar, vector.y / scalar, vector.z / scalar);
    }

    public static double scalar(Vector3D v1, Vector3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static double angleRadians(Vector3D v1, Vector3D v2) {
        return Math.acos(scalar(v1, v2) / (getLength(v1) * getLength(v2)));
    }

    public static double angleDegrees(Vector3D v1, Vector3D v2) {
        return RAD_TO_DEG * angleRadians(v1, v2);
    }

    public Vector3D() {
        y = 0.0;
        x = 0.0;
        z = 0.0;
    }

    public Vector3D(double x, double y, double z) {
        set(x, y, z);
    }

    public Vector3D(Vector3D vector) {
        set(vector);
    }

    public boolean isZero() {
        return x == 0.0 || y == 0.0;
    }

    public Vector3D set(Vector3D vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        return this;
    }

    public Vector3D set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3D sumX(double x) {
        this.x += x;
        return this;
    }

    public Vector3D sumY(double y) {
        this.y += y;
        return this;
    }

    public Vector3D sumZ(double z) {
        this.z += z;
        return this;
    }

    public Vector3D sum(Vector3D vector) {
        sumX(vector.x);
        sumY(vector.y);
        sumZ(vector.z);
        return this;
    }

    public Vector3D sumNew(Vector3D vector) {
        var v = new Vector3D(this);
        v.sumX(vector.x);
        v.sumY(vector.y);
        v.sumZ(vector.z);
        return v;
    }

    public Vector3D sum(double x, double y, double z) {
        sumX(x);
        sumY(y);
        sumZ(z);
        return this;
    }

    public Vector3D sumNew(double x, double y, double z) {
        var v = new Vector3D(this);
        v.sumX(x);
        v.sumY(y);
        v.sumZ(z);
        return v;
    }

    public Vector3D multiplyX(double x) {
        this.x *= x;
        return this;
    }

    public Vector3D multiplyY(double y) {
        this.y *= y;
        return this;
    }

    public Vector3D multiplyZ(double z) {
        this.z *= z;
        return this;
    }

    public Vector3D multiply(Vector3D vector) {
        multiplyX(vector.x);
        multiplyY(vector.y);
        multiplyZ(vector.z);
        return this;
    }

    public Vector3D multiply(double x, double y, double z) {
        multiplyX(x);
        multiplyY(y);
        multiplyZ(z);
        return this;
    }

    public Vector3D divideX(double x) {
        this.x /= x;
        return this;
    }

    public Vector3D divideY(double y) {
        this.y /= y;
        return this;
    }

    public Vector3D divideZ(double z) {
        this.z /= z;
        return this;
    }

    public Vector3D divide(Vector3D vector) {
        divideX(vector.x);
        divideY(vector.y);
        divideZ(vector.z);
        return this;
    }

    public Vector3D divide(double x, double y, double z) {
        divideX(x);
        divideY(y);
        divideZ(z);
        return this;
    }

    public Vector3D diffX(double x) {
        this.x -= x;
        return this;
    }

    public Vector3D diffY(double y) {
        this.y -= y;
        return this;
    }

    public Vector3D diffZ(double z) {
        this.z -= z;
        return this;
    }

    public Vector3D diff(Vector3D vector) {
        diff(vector.x, vector.y, vector.z);
        return this;
    }

    public Vector3D diff(double x, double y, double z) {
        diffX(x);
        diffY(y);
        diffZ(z);
        return this;
    }

    public Vector3D reduce(double value) {
        var valueNew = Math.abs(value);
        if (x > 0) diffX(valueNew);
        else if (x < 0) sumX(valueNew);

        if (y > 0) diffY(valueNew);
        else if (y < 0) sumY(valueNew);

        if (z > 0) diffZ(valueNew);
        else if (z < 0) sumZ(valueNew);
        return this;
    }

    public Vector3D inverse() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public Vector3D normalize() {
        set(normalize(this));
        return this;
    }

    public Vector3D normalizeNew() {
        return normalize(this);
    }

    public Vector3D inverseNew() {
        return new Vector3D(-x, -y, -z);
    }

    public void inverse(double multiplicator) {
        x = (-multiplicator * x);
        y = (-multiplicator * y);
        z = (-multiplicator * z);
    }
}
