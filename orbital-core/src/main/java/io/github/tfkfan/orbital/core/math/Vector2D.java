package io.github.tfkfan.orbital.core.math;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector2D implements Vector<Vector2D> {
    private double x = 0.0;
    private double y = 0.0;

    public static final double RAD_TO_DEG = 180 / Math.PI;
    public static final Vector2D REFERENCE = new Vector2D(1.0, 0.0);

    public static Vector2D abs(Vector2D vector) {
        return new Vector2D(Math.abs(vector.getX()), Math.abs(vector.getY()));
    }

    public static Vector2D diff(Vector2D v1, Vector2D v2) {
        return new Vector2D(v2.x - v1.x, v2.y - v1.y);
    }

    public static double getDistance(Vector2D vector1, Vector2D vector2) {
        return Math.sqrt(Math.pow((vector2.x - vector1.x), 2.0) + Math.pow((vector2.y - vector1.y), 2.0));
    }

    public static double length(Vector2D vector) {
        return Math.sqrt(Math.pow(vector.x, 2.0) + Math.pow(vector.y, 2.0));
    }

    public static Vector2D normalize(Vector2D v1) {
        var length = length(v1);
        if (length != 0.0) return new Vector2D(v1.x / length, v1.y / length);

        return new Vector2D(0.0, 0.0);
    }

    public static Vector2D mult(Vector2D vector, double scalar) {
        return new Vector2D(vector.x * scalar, vector.y * scalar);
    }

    public static Vector2D div(Vector2D vector, double scalar) {
        return new Vector2D(vector.x / scalar, vector.y / scalar);
    }

    public static double scalar(Vector2D v1, Vector2D v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static double angleRadians(Vector2D v1, Vector2D v2) {
        return Math.acos(scalar(v1, v2) / (length(v1) * length(v2)));
    }

    public static double angleDegrees(Vector2D v1, Vector2D v2) {
        return RAD_TO_DEG * angleRadians(v1, v2);
    }

    public Vector2D() {
        y = 0.0;
        x = 0.0;
    }

    public Vector2D(double x, double y) {
        set(x, y);
    }

    public Vector2D(Vector2D vector) {
        set(vector);
    }

    @Override
    public boolean isZero() {
        return x == 0.0 || y == 0.0;
    }

    @Override
    public Vector2D set(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;
        return this;
    }

    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D sumX(double x) {
        this.x += x;
        return this;
    }

    public Vector2D sumY(double y) {
        this.y += y;
        return this;
    }

    @Override
    public Vector2D sum(Vector2D vector) {
        sumX(vector.x);
        sumY(vector.y);
        return this;
    }

    public Vector2D sum(double x, double y) {
        sumX(x);
        sumY(y);
        return this;
    }

    public Vector2D nsum(double x, double y) {
        var v = new Vector2D(this);
        v.sumX(x);
        v.sumY(y);
        return v;
    }

    @Override
    public Vector2D nsum(Vector2D vector) {
        var v = new Vector2D(this);
        v.sumX(vector.x);
        v.sumY(vector.y);
        return v;
    }

    public Vector2D multX(double x) {
        this.x *= x;
        return this;
    }

    public Vector2D multY(double y) {
        this.y *= y;
        return this;
    }

    @Override
    public Vector2D mult(Vector2D vector) {
        multX(vector.x);
        multY(vector.y);
        return this;
    }

    public Vector2D mult(double x, double y) {
        multX(x);
        multY(y);
        return this;
    }

    public Vector2D nmult(double x, double y) {
        var v = new Vector2D(this);
        v.multX(x);
        v.multY(y);
        return v;
    }

    @Override
    public Vector2D nmult(Vector2D vector) {
        var v = new Vector2D(this);
        v.multX(vector.x);
        v.multY(vector.y);
        return v;
    }

    public Vector2D divX(double x) {
        this.x /= x;
        return this;
    }

    public Vector2D divY(double y) {
        this.y /= y;
        return this;
    }

    @Override
    public Vector2D div(Vector2D vector) {
        divX(vector.x);
        divY(vector.y);
        return this;
    }

    public Vector2D div(double x, double y) {
        divX(x);
        divY(y);
        return this;
    }

    public Vector2D ndiv(double x, double y) {
        var v = new Vector2D(this);
        v.divX(x);
        v.divY(y);
        return v;
    }

    @Override
    public Vector2D ndiv(Vector2D vector) {
        var v = new Vector2D(this);
        v.divX(vector.x);
        v.divY(vector.y);
        return v;
    }

    @Override
    public Vector2D diff(Vector2D vector) {
        diff(vector.x, vector.y);
        return this;
    }

    public Vector2D diff(double x, double y) {
        diffX(x);
        diffY(y);
        return this;
    }

    public Vector2D diffX(double x) {
        this.x -= x;
        return this;
    }

    public Vector2D diffY(double y) {
        this.y -= y;
        return this;
    }

    public Vector2D ndiff(double x, double y) {
        var v = new Vector2D(this);
        v.diffX(x);
        v.diffY(y);
        return v;
    }

    @Override
    public Vector2D ndiff(Vector2D vector) {
        var v = new Vector2D(this);
        v.diffX(vector.x);
        v.diffY(vector.y);
        return v;
    }

    @Override
    public Vector2D reduce(double value) {
        var valueNew = Math.abs(value);
        if (x > 0) diffX(valueNew);
        else if (x < 0) sumX(valueNew);

        if (y > 0) diffY(valueNew);
        else if (y < 0) sumY(valueNew);
        return this;
    }

    @Override
    public Vector2D inverse() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    @Override
    public Vector2D inverse(double multiplicator) {
        x = (-multiplicator * x);
        y = (-multiplicator * y);
        return this;
    }

    @Override
    public Vector2D normalize() {
        set(normalize(this));
        return this;
    }

    @Override
    public double length() {
        return length(this);
    }

    @Override
    public Vector2D clone() {
        try {
            return (Vector2D) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
