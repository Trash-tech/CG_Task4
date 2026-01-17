package com.cgvsu.math.LinearAlgebra;

public class Vector2D extends Vector<Vector2D>{
    public Vector2D(float [] data){
        super(data, 2);
        if (data.length != 2){
            throw new IllegalArgumentException("Вы пытаетесь создать вектор 2-ой размерности, но передаете массив неправильной длины!");
        }
    }


    public Vector2D(float x, float y){
        super(new float[] {x, y}, 2);
    }


    @Override
    protected Vector2D createNew(float[] data) {
        return new Vector2D(data);
    }
}
