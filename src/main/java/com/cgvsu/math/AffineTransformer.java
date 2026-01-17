package com.cgvsu.math;

import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import com.cgvsu.math.LinearAlgebra.Vector3D;

import java.util.ArrayList;

public class AffineTransformer {
    Matrix4x4 modelMatrix;
    float sx = 1.0f, sy = 1.0f, sz = 1.0f;
    float angleRotateX = 0.0f, angleRotateY = 0.0f, angleRotateZ = 0.0f;
    float tx = 0.0f, ty = 0.0f, tz = 0.0f;

    public AffineTransformer(){
        modelMatrix = Matrix4x4.oneMatrix();
    }

    public float getScaleX() {
        return sx;
    }
    public float getScaleY() {
        return sy;
    }
    public float getScaleZ() {
        return sz;
    }

    public float getRotationXDegrees() {
        return toDegree(angleRotateX);
    }
    public float getRotationYDegrees() {
        return toDegree(angleRotateY);
    }
    public float getRotationZDegrees() {
        return toDegree(angleRotateZ);
    }

    public float getRotationXRadians() {
        return angleRotateX;
    }
    public float getRotationYRadians() {
        return angleRotateY;
    }
    public float getRotationZRadians() {
        return angleRotateZ;
    }

    public float getTranslationX() {
        return tx;
    }
    public float getTranslationY() {
        return ty;
    }
    public float getTranslationZ() {
        return tz;
    }


    public void changeModelMatrixForScaling(float sx, float sy, float sz){
        modelMatrix = ModelMatrix(
                sx, sy, sz,
                angleRotateX,
                angleRotateY,
                angleRotateZ,
                tx, ty, tz);
    }

    public void changeModelMatrixTranslation(float tx, float ty, float tz){
        modelMatrix = ModelMatrix(
                sx, sy, sz,
                angleRotateX,
                angleRotateY,
                angleRotateZ,
                tx, ty, tz);
    }

    public void changeModelMatrixForDegree(float angleRotateX, float angleRotateY, float angleRotateZ){
        modelMatrix = ModelMatrix(
                sx, sy, sz,
                toRadians(angleRotateX),
                toRadians(angleRotateY),
                toRadians(angleRotateZ),
                tx, ty, tz);
    }

    public void changeModelMatrixForRadians(float angleRotateX, float angleRotateY, float angleRotateZ){
        modelMatrix = ModelMatrix(
                sx, sy, sz,
                angleRotateX,
                angleRotateY,
                angleRotateZ,
                tx, ty, tz);
    }

    public void changeModelMatrix(){
        modelMatrix = ModelMatrix(
                sx, sy, sz,
                angleRotateX,
                angleRotateY,
                angleRotateZ,
                tx, ty, tz);
    }

    public void changeModelMatrixForRadians(
            float sx,
            float sy,
            float sz,
            float angleRotateX,
            float angleRotateY,
            float angleRotateZ,
            float tx,
            float ty,
            float tz) {
        modelMatrix = ModelMatrix(
                sx, sy, sz,
                angleRotateX,
                angleRotateY,
                angleRotateZ,
                tx, ty, tz);
    }

    public void changeModelMatrixForDegree(
            float sx,
            float sy,
            float sz,
            float angleRotateX,
            float angleRotateY,
            float angleRotateZ,
            float tx,
            float ty,
            float tz) {
        modelMatrix = ModelMatrix(
                sx, sy, sz,
                toRadians(angleRotateX),
                toRadians(angleRotateY),
                toRadians(angleRotateZ),
                tx, ty, tz);
    }

    public Matrix4x4 getModelMatrix() {
        return modelMatrix;
    }

    public static Matrix4x4 createScalingMatrix(float sx, float sy, float sz) {
        return new Matrix4x4(new float[][]{
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 createRotateXMatrix(float angleRotateX) {
        return new Matrix4x4(new float[][]{
                {1, 0, 0, 0},
                {0, (float) Math.cos(angleRotateX), (float) Math.sin(angleRotateX), 0},
                {0, (float) -Math.sin(angleRotateX), (float) Math.cos(angleRotateX), 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 createRotateYMatrix(float angleRotateY) {
        return new Matrix4x4(new float[][]{
                {(float) Math.cos(angleRotateY), 0, (float) Math.sin(angleRotateY), 0},
                {0, 1, 0, 0},
                {(float) - Math.sin(angleRotateY), 0, (float) Math.cos(angleRotateY), 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 createRotateZMatrix(float angleRotateZ) {
        return new Matrix4x4(new float[][]{
                {(float) Math.cos(angleRotateZ), (float) Math.sin(angleRotateZ), 0, 0},
                {(float) -Math.sin(angleRotateZ), (float) Math.cos(angleRotateZ), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    public static Matrix4x4 createRotateMatrix(float angleRotateX, float angleRotateY, float angleRotateZ) {
        return createRotateZMatrix(angleRotateZ).
                multiply(createRotateYMatrix(angleRotateY)).
                multiply(createRotateXMatrix(angleRotateX));
    }

    public static Matrix4x4 createTranslationMatrix(float tx, float ty, float tz) {
        return new Matrix4x4(new float[][]{
                {1, 0, 0, tx},
                {0, 1, 0, ty},
                {0, 0, 1, tz},
                {0, 0, 0, 1}
        });
    }

    public Matrix4x4 ModelMatrix(float sx,
                                       float sy,
                                       float sz,
                                       float angleRotateX,
                                       float angleRotateY,
                                       float angleRotateZ,
                                       float tx,
                                       float ty,
                                       float tz){
        safeValues(
                sx, sy, sz,
                angleRotateX,
                angleRotateY,
                angleRotateZ,
                tx, ty, tz);
        return createModelMatrix(
                sx, sy, sz,
                angleRotateX,
                angleRotateY,
                angleRotateZ,
                tx, ty, tz);
    }

    public static Matrix4x4 createModelMatrix(
            float sx,
            float sy,
            float sz,
            float angleRotateX,
            float angleRotateY,
            float angleRotateZ,
            float tx,
            float ty,
            float tz) {
        return createTranslationMatrix(tx, ty, tz).
                multiply(createRotateMatrix(angleRotateX, angleRotateY, angleRotateZ)).
                multiply(createScalingMatrix(sx, sy, sz));
    }

    public void safeValues(float sx,
                           float sy,
                           float sz,
                           float angleRotateX,
                           float angleRotateY,
                           float angleRotateZ,
                           float tx,
                           float ty,
                           float tz){
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.angleRotateX = angleRotateX;
        this.angleRotateY = angleRotateY;
        this.angleRotateZ = angleRotateZ;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
    }

    public ArrayList<Vector3D> verticesTransformation(ArrayList<Vector3D> vertices) {
        for (int i = 0; i < vertices.size(); i++) {
            Vector3D vertex = vertices.get(i);
            Vector3D preResult = modelMatrix.multiplyByVector(vertex);
            vertices.set(i, preResult);
        }
        return vertices;
    }

    public ArrayList<Vector3D> normalTransformation(ArrayList<Vector3D> normals){
        for (int i = 0; i < normals.size(); i++){
            Vector3D normal = normals.get(i);
            Vector3D preResult = modelMatrix.toMatrix3x3()
                    .inverse().transpose().multiplyByVector(normal).normalization();
            normals.set(i, preResult);
        }
        return normals;
    }

    public static float toDegree(float radicals){
        return radicals * 180.0f / (float)Math.PI;
    }

    public static float toRadians(float degree){
        return (float) Math.PI * degree / 180;
    }
}