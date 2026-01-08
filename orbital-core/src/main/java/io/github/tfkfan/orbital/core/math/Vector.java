package io.github.tfkfan.orbital.core.math;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public interface Vector<V extends Vector<V>> extends Serializable, Cloneable {
    @JsonIgnore
    boolean isZero();

    V set(V vector);

    V sum(V vector);

    V nsum(V vector);

    V mult(V vector);

    V nmult(V vector);

    V div(V vector);

    V ndiv(V vector);

    V diff(V vector);

    V ndiff(V vector);

    V reduce(double value);

    V inverse();

    V inverse(double multiplicator);

    V normalize();

    double length();

    V clone();
}
