package com.cgvsu.math.LinearAlgebra;

public abstract class LinearAlgebraObject {
    private static final float eps = 1e-9f;
    public LinearAlgebraObject (int dimension){
        this.dimension = dimension;
    }
    private final int dimension;

    public int getDimension() {
        return dimension;
    }


    //public abstract float [][] getData();
    public static float abs(float a){
        if (a < 0){
            return -a;
        }
        return a;
    }


    public static boolean floatIsNotZero(float a){
        return abs(a) > eps;
    }


    public static boolean floatIsZero(float a){
        return abs(a) < eps;
    }
}
