package com.cgvsu.math;

import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import com.cgvsu.math.LinearAlgebra.Vector3D;
import com.cgvsu.model.Model;

import java.util.ArrayList;

public class AffineTransformer {
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

    public static void modelTransformation(
            Model model,
            float sx,
            float sy,
            float sz,
            float angleRotateX,
            float angleRotateY,
            float angleRotateZ,
            float tx,
            float ty,
            float tz) {
        Matrix4x4 modelMatrix = createModelMatrix(sx, sy, sz,
        angleRotateX, angleRotateY, angleRotateZ, tx, ty, tz);
        modelTransformation(model, modelMatrix);
    }

    public static void modelTransformation(Model model, Matrix4x4 modelMatrix){
        model.setVertices(verticesTransformation(model.getVertices(), modelMatrix));
        model.setNormals(normalTransformation(model.getNormals(), modelMatrix));
    }

    public static ArrayList<Vector3D> verticesTransformation(ArrayList<Vector3D> vertices, Matrix4x4 modelMatrix) {
        //ArrayList<Vector3D> vertices = model.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3D vertex = vertices.get(i);
            Vector3D preResult = modelMatrix.multiplyByVector(vertex);
            vertices.set(i, preResult);
        }
        return vertices;
    }

    public static ArrayList<Vector3D> normalTransformation(ArrayList<Vector3D> normals, Matrix4x4 modelMatrix){
        //ArrayList<Vector3D> normals = model.getNormals();
        for (int i = 0; i < normals.size(); i++){
            Vector3D normal = normals.get(i);
            Vector3D preResult = modelMatrix.toMatrix3x3()
                    .inverse().transpose().multiplyByVector(normal).normalization();
            normals.set(i, preResult);
        }
        return normals;
    }

    public static float toDegree(float radicals){
        return (float) Math.PI * radicals / 180;
    }

    public static float toRadicals(float degree){
        return (float) Math.PI * degree / 180;
    }
}