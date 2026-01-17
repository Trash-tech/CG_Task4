package com.cgvsu.math.LinearAlgebra;

public class Matrix3x3 extends Matrix<Matrix3x3>{
    public Matrix3x3(float [][] data){
        super(data, 3);
        if (data.length != 3 || data[0].length != 3){
            throw new IllegalArgumentException("Вы пытаетесь создать матрицу 3x3, но передаете массив неправильной длины!");
        }
    }


    @Override
    protected Matrix3x3 createNew(float[][] data) {
        return new  Matrix3x3(data);
    }


    public static Matrix3x3 oneMatrix() {
        return new Matrix3x3(new float[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        });
    }


    public static Matrix3x3 zeroMatrix() {
        return new Matrix3x3(new float[3][3]);
    }


    public Vector3D multiplyByVector(Vector4D vector4D){
        if (this.getDimension() == 3) {
            float x = vector4D.getX(),
                    y = vector4D.getY(),
                    z = vector4D.getZ(),
                    w = vector4D.getW();
            if (floatIsZero(w)) {
                w = 1;
            }
            float[] data = {x / w, y / w, z / w};
            return this.multiplyByVector(new Vector3D(data));
        }else {
            throw new IllegalArgumentException("Вы вызывайте не тот метод этот метод для умножения матрицы 3x3 на вектор 4д");
        }
    }

    public float det(){
        float[][] data = this.getData();
        return data[0][0] * data[1][1] * data[2][2] +
                data[2][0] * data[0][1] * data[1][2] +
                data[1][0] * data[2][1] * data[0][2] -
                data[2][0] * data[1][1] * data[0][2] -
                data[0][0] * data[2][1] * data[1][2] -
                data[1][0] * data[0][1] * data[2][2];
    }

    public Matrix3x3 inverse(){
        float det = this.det();
        if (floatIsZero(det)){
            throw new ArithmeticException("Матрица вырождена, обратной не существует");
        }else {
            float[][] data = this.getData();
            float m00 = data[1][1] * data[2][2] - data[1][2] * data[2][1];
            float m01 = data[1][0] * data[2][2] - data[1][2] * data[2][0];
            float m02 = data[1][0] * data[2][1] - data[1][1] * data[2][0];

            float m10 = data[0][1] * data[2][2] - data[0][2] * data[2][1];
            float m11 = data[0][0] * data[2][2] - data[0][2] * data[2][0];
            float m12 = data[0][0] * data[2][1] - data[0][1] * data[2][0];

            float m20 = data[0][1] * data[1][2] - data[0][2] * data[1][1];
            float m21 = data[0][0] * data[1][2] - data[0][2] * data[1][0];
            float m22 = data[0][0] * data[1][1] - data[0][1] * data[1][0];

            return new Matrix3x3(new float[][] {
                    { m00, -m10,  m20},
                    {-m01,  m11, -m21},
                    { m02, -m12,  m22}
            }).divisionByScalar(det);
        }
    }
}
