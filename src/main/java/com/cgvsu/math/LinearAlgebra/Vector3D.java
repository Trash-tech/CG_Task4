package com.cgvsu.math.LinearAlgebra;

public class Vector3D extends Vector<Vector3D>{
    public Vector3D(float [] data){
        super(data, 3);
        if (data.length != 3){
            throw new IllegalArgumentException("Вы пытаетесь создать вектор 3-ой размерности, но передаете массив неправильной длины!");
        }
    }


    public Vector3D(float x, float y, float z){
        super(new float[] {x, y, z}, 3);
    }


    @Override
    protected Vector3D createNew(float[] data) {
        return new Vector3D(data);
    }


    public float getZ(){
        return getDataByIndex(2);
    }


    public Vector3D crossProduct(Vector3D v2){
        float[] resultData = new float[3];
        resultData[0] = this.getY() * v2.getZ() - this.getZ() * v2.getY();
        resultData[1] = this.getZ() * v2.getX() - this.getX() * v2.getZ();
        resultData[2] = this.getX() * v2.getY() - this.getY() * v2.getX();
        return new Vector3D(resultData);
    }
}
