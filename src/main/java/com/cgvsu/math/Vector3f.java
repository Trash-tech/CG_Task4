package com.cgvsu.math;

import java.util.Objects;

public class Vector3f {
    private static final float EPSILON = 1e-9f;
    private float x;
    private float y;
    private float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector3f vector3f = (Vector3f) obj;
        return Float.compare(vector3f.x, x) == 0 && Float.compare(vector3f.y, y) == 0
                && Float.compare(vector3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Float.hashCode(x),
                Float.hashCode(y),
                Float.hashCode(z)
        );
    }

    public String toString() {
        return String.format("Vector3f(%.3f, %.3f, %.3f)", x, y, z);
    }

    public float getX()  {return x;}

    public float getY()  {return y;}

    public float getZ()  {return z;}

}
