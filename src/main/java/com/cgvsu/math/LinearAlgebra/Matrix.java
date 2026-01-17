package com.cgvsu.math.LinearAlgebra;

import java.util.Arrays;

public abstract class Matrix<T extends Matrix<T>> extends LinearAlgebraObject{
    private final float[][] data;


    public Matrix(float[][] data, int dimension){
        super(dimension);
        if(data.length == dimension && data[0].length == dimension) {
            this.data = data;
        }else {
            throw new IllegalArgumentException("Длина двумерного массива не совпадает с указанной размерностью");
        }
    }

    public float[][] getData() {
        return data.clone();
    }

    protected abstract T createNew(float[][] data);
    //public abstract T zeroMatrix();
    //public abstract T oneMatrix();


    public T sum(T m2) {
        float[][] m2Data = m2.getData();
        float[][] resultData = new float[this.data.length][this.data.length];
        for (int i = 0; i < this.data.length; i++){
            for (int j = 0; j < this.data[i].length; j++) {
                resultData[i][j] = this.data[i][j] + m2Data[i][j];
            }
        }
        return createNew(resultData);
    }


    public T subtract(T m2) {
        float[][] m2Data = m2.getData();
        float[][] resultData = new float[this.data.length][this.data.length];
        for (int i = 0; i < this.data.length; i++){
            for (int j = 0; j < this.data[i].length; j++) {
                resultData[i][j] = this.data[i][j] - m2Data[i][j];
            }
        }
        return createNew(resultData);
    }


    public T multiply(T m2) {
        float[][] m2Data = m2.getData();
        float[][] resultData = new float[this.data.length][this.data.length];
        for (int i = 0; i < this.data.length; i++){
            for (int j = 0; j < this.data[i].length; j++) {
                float sum = 0;
                for (int k = 0; k < this.data.length; k++){
                    sum += this.data[i][k] * m2Data[k][j];
                }
                resultData[i][j] = sum;
            }
        }
        return createNew(resultData);
    }

    public T multiplyByScalar(float scalar) {
        float[][] resultData = new float[this.data.length][this.data.length];
        for (int j = 0; j < this.data.length; j++) {
            for (int i = 0; i < this.data.length; i++) {
                resultData[j][i] = this.data[j][i] * scalar;
            }
        }
        return createNew(resultData);
    }

    public T divisionByScalar(float scalar) {
        if (LinearAlgebraObject.floatIsNotZero(scalar)) {
            return multiplyByScalar(1 / scalar);
        }else {
            throw new IllegalArgumentException("Скаляр равен нулю, а вы на него делите!");
        }
    }

    public T transpose() {
        float[][] resultData = new float[this.data.length][this.data.length];
        for (int i = 0; i < this.data.length; i++){
            for (int j = 0; j < this.data[i].length; j++) {
                resultData[i][j] = this.data[j][i];
            }
        }
        return createNew(resultData);
    }


    public <V extends Vector<V>>V multiplyByVector(V vector) {
        if (this.getDimension() == vector.getDimension()) {
            float[] v2Data = vector.getData();
            float[] resultData = new float[this.data.length];
            for (int i = 0; i < this.data.length; i++) {
                float sum = 0;
                for (int j = 0; j < this.data[i].length; j++) {
                    sum += this.data[i][j] * v2Data[j];
                }
                resultData[i] = sum;
            }
            return vector.createNew(resultData);
        }else {
            throw new IllegalArgumentException("Не поддерживаемые размерности!");
        }
    }


    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }else {
            Matrix<?> other = (Matrix<?>) obj;
            for (int i = 0; i < data.length; i++){
                if (!Arrays.equals(other.data[i], this.data[i])){
                    return false;
                }
            }
            return true;
        }
    }


    @Override
    public String toString(){
        String result = "";
        for (int i = 0; i < data.length; i++){
            result += Arrays.toString(this.data[i])+"\n";
        }
        return result;
    }
}
