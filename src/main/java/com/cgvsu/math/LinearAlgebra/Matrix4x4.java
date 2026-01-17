package com.cgvsu.math.LinearAlgebra;

public class Matrix4x4 extends Matrix<Matrix4x4>{
    public Matrix4x4(float [][] data){
        super(data, 4);
        if (data.length != 4 || data[0].length != 4){
            throw new IllegalArgumentException("Вы пытаетесь создать матрицу 4x4, но передаете массив неправильной длины!");
        }
    }

    public Matrix4x4(Matrix4x4 matrix4x4){
        super(matrix4x4.getData(), 4);
        if (matrix4x4.getData().length != 4 || matrix4x4.getData()[0].length != 4){
            throw new IllegalArgumentException("Вы пытаетесь создать матрицу 4x4, но передаете массив неправильной длины!");
        }
    }
    @Override
    protected Matrix4x4 createNew(float[][] data) {
        return new  Matrix4x4(data);
    }


    public static Matrix4x4 oneMatrix() {
        return new Matrix4x4(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }


    public static Matrix4x4 zeroMatrix() {
        return new Matrix4x4(new float[4][4]);
    }


    public Vector3D multiplyByVector(Vector3D vector3D){
        if (this.getDimension() == 4) {
            Vector4D preResultVector = this.multiplyByVector(new Vector4D(
                    new float[]{
                            vector3D.getX(),
                            vector3D.getY(),
                            vector3D.getZ(),
                            (float) 1
                    }
            ));
            float x = preResultVector.getX(),
                    y = preResultVector.getY(),
                    z = preResultVector.getZ(),
                    w = preResultVector.getW();
            if (floatIsZero(w)) {
                w = 1;
            }
            return new Vector3D(new float[]{x / w, y / w, z / w});
        }else {
            throw new IllegalArgumentException("Вы вызывайте не тот метод этот метод для умножения матрицы 4x4 на вектор 3д");
        }
    }

    public Matrix3x3 toMatrix3x3(){
        float[][] data = this.getData();
        return new Matrix3x3(new float[][]{
                {data[0][0], data[0][1], data[0][2]},
                {data[1][0], data[1][1], data[1][2]},
                {data[2][0], data[2][1], data[2][2]}
        });
    }
}
