package com.cgvsu.math;

import java.util.Objects;

public class Vector2f {
    private static final float EPSILON = 1e-9f;
    private float x;
    private float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2f vector2f = (Vector2f) obj;
        return Float.compare(vector2f.x, x) == 0 &&
                Float.compare(vector2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Float.hashCode(x),
                Float.hashCode(y)
        );
    }

    @Override
    public String toString() {
        return String.format("Vector2f(%f, %f)", x, y);
    }

    public float getX()  {return x;}

    public float getY()  {return y;}

}
