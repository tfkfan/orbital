package io.github.tfkfan.orbital.core.math;


public class Vector3D implements Vector<Vector3D> {
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    public static final double RAD_TO_DEG = 180 / Math.PI;
    public static final Vector3D REFERENCE = new Vector3D(1.0, 0.0, 0.0);

    public static Vector3D diff(Vector3D v1, Vector3D v2) {
        return new Vector3D(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }

    public static double distance(Vector3D vector1, Vector3D vector2) {
        return Math.sqrt(Math.pow((vector2.x - vector1.x), 2.0) + Math.pow((vector2.y - vector1.y), 2.0) + Math.pow((vector2.z - vector1.z), 2.0));
    }

    public static double length(Vector3D vector) {
        return Math.sqrt(Math.pow(vector.x, 2.0) + Math.pow(vector.y, 2.0) + Math.pow(vector.z, 2.0));
    }

    public static Vector3D normalize(Vector3D v1) {
        var length = length(v1);
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
        return Math.acos(scalar(v1, v2) / (length(v1) * length(v2)));
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

    public double getX() {
        return x;
    }

    public Vector3D setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Vector3D setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public Vector3D setZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public boolean isZero() {
        return x == 0.0 || y == 0.0;
    }

    @Override
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

    @Override
    public Vector3D sum(Vector3D vector) {
        sumX(vector.x);
        sumY(vector.y);
        sumZ(vector.z);
        return this;
    }

    @Override
    public Vector3D nsum(Vector3D vector) {
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

    public Vector3D nsum(double x, double y, double z) {
        var v = new Vector3D(this);
        v.sumX(x);
        v.sumY(y);
        v.sumZ(z);
        return v;
    }

    public Vector3D multX(double x) {
        this.x *= x;
        return this;
    }

    public Vector3D multY(double y) {
        this.y *= y;
        return this;
    }

    public Vector3D multZ(double z) {
        this.z *= z;
        return this;
    }

    @Override
    public Vector3D mult(Vector3D vector) {
        multX(vector.x);
        multY(vector.y);
        multZ(vector.z);
        return this;
    }

    @Override
    public Vector3D nmult(Vector3D vector) {
        var v = new Vector3D(this);
        v.multX(vector.x);
        v.multY(vector.y);
        v.multZ(vector.z);
        return v;
    }

    public Vector3D mult(double x, double y, double z) {
        multX(x);
        multY(y);
        multZ(z);
        return this;
    }

    public Vector3D divX(double x) {
        this.x /= x;
        return this;
    }

    public Vector3D divY(double y) {
        this.y /= y;
        return this;
    }

    public Vector3D divZ(double z) {
        this.z /= z;
        return this;
    }

    @Override
    public Vector3D div(Vector3D vector) {
        divX(vector.x);
        divY(vector.y);
        divZ(vector.z);
        return this;
    }

    @Override
    public Vector3D ndiv(Vector3D vector) {
        var v = new Vector3D(this);
        v.divX(vector.x);
        v.divY(vector.y);
        v.divZ(vector.z);
        return v;
    }

    public Vector3D div(double x, double y, double z) {
        divX(x);
        divY(y);
        divZ(z);
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

    @Override
    public Vector3D diff(Vector3D vector) {
        diff(vector.x, vector.y, vector.z);
        return this;
    }

    @Override
    public Vector3D ndiff(Vector3D vector) {
        var v = new Vector3D(this);
        v.diffX(vector.x);
        v.diffY(vector.y);
        v.diffZ(vector.z);
        return v;
    }

    public Vector3D diff(double x, double y, double z) {
        diffX(x);
        diffY(y);
        diffZ(z);
        return this;
    }

    @Override
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

    @Override
    public Vector3D inverse() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    @Override
    public Vector3D normalize() {
        set(normalize(this));
        return this;
    }

    @Override
    public double length() {
        return length(this);
    }

    @Override
    public Vector3D inverse(double multiplicator) {
        x = (-multiplicator * x);
        y = (-multiplicator * y);
        z = (-multiplicator * z);
        return this;
    }
}
