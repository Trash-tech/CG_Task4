package com.cgvsu.math;

import org.junit.jupiter.api.Test;
import com.cgvsu.math.LinearAlgebra.Matrix3x3;
import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import com.cgvsu.math.LinearAlgebra.Vector3D;
import com.cgvsu.model.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class AffineTransformerTest {
    private static final float EPC = 0.0001f;

    @Test
    public void testCreateScalingMatrix() {
        float sx = 10, sy = 10, sz = 10;
        Matrix4x4 result = AffineTransformer.createScalingMatrix(sx, sy, sz);
        Matrix4x4 expected = new Matrix4x4(new float[][]{
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}
        });
        assertEquals(expected, result);
    }

    @Test
    public void testCreateRotateXMatrix() {
        float angelRotateX = 10;
        Matrix4x4 result = AffineTransformer.createRotateXMatrix(10);
        Matrix4x4 expected = new Matrix4x4(new float[][]{
                {1, 0, 0, 0},
                {0, (float) Math.cos(angelRotateX), (float) Math.sin(angelRotateX), 0},
                {0, (float) -Math.sin(angelRotateX), (float) Math.cos(angelRotateX), 0},
                {0, 0, 0, 1}
        });
        assertEquals(expected, result);
    }

    @Test
    public void testCreateRotateYMatrix() {
        float angelRotateY = 10;
        Matrix4x4 result = AffineTransformer.createRotateYMatrix(10);
        Matrix4x4 expected = new Matrix4x4(new float[][]{
                {(float) Math.cos(angelRotateY), 0, (float) Math.sin(angelRotateY), 0},
                {0, 1, 0, 0},
                {(float) - Math.sin(angelRotateY), 0, (float) Math.cos(angelRotateY), 0},
                {0, 0, 0, 1}
        });
        assertEquals(expected, result);
    }

    @Test
    public void testCreateRotateZMatrix() {
        float angelRotateZ = 10;
        Matrix4x4 result = AffineTransformer.createRotateZMatrix(10);
        Matrix4x4 expected = new Matrix4x4(new float[][]{
                {(float) Math.cos(angelRotateZ), (float) Math.sin(angelRotateZ), 0, 0},
                {(float) -Math.sin(angelRotateZ), (float) Math.cos(angelRotateZ), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        assertEquals(expected, result);
    }

    @Test
    public void testCreateTranslationMatrix() {
        float tx = 10, ty = 10, tz = 10;
        Matrix4x4 result = AffineTransformer.createTranslationMatrix(tx, ty, tz);
        Matrix4x4 expected = new Matrix4x4(new float[][]{
                {1, 0, 0, tx},
                {0, 1, 0, ty},
                {0, 0, 1, tz},
                {0, 0, 0, 1}
        });
        assertEquals(expected, result);
    }

    @Test
    public void testCreateRotateMatrix() {
        /*
        return createRotateZMatrix(angelRotateZ).
                multiply(createRotateYMatrix(angelRotateY)).
                multiply(createRotateXMatrix(angelRotateX));

         */
    }

    @Test
    public void testCreateFinalTransformationMatrix() {
        /*
        return createTranslationMatrix(tx, ty, tz).
                multiply(createRotateMatrix(angelRotateX, angelRotateY, angelRotateZ)).
                multiply(createScalingMatrix(sx, sy, sz));

         */
    }

    @Test
    public void testVerticesTransformation() {
            Model model = new Model();
            ArrayList<Vector3D> modelVertices = new ArrayList<>();
            modelVertices.add(new Vector3D(new float[]{1,2,3}));
            model.setVertices(modelVertices);

            Matrix4x4 translation = AffineTransformer.createTranslationMatrix(5, 0, 0);

            AffineTransformer transformer = new AffineTransformer();
            transformer.modelTransformation(model, translation);

            Vector3D vertex = model.getVertices().get(0);
            assertEquals(6, vertex.getX(), EPC);
            assertEquals(2, vertex.getY(), EPC);
            assertEquals(3, vertex.getZ(), EPC);
    }

    @Test
    public void testNormalTransformation(){
        /*
        ArrayList<Vector3D> normals = model.getNormals();
        for (int i = 0; i < normals.size(); i++){
            Vector3D normal = normals.get(i);
            Vector3D preResult = Matrix4x4ToMatrix3x3(finalTransformationMatrix)
                    .inverse().transpose().multiplyByVector(normal);
            normals.set(i, preResult);
        }
        model.setNormals(normals);

         */
    }
}
