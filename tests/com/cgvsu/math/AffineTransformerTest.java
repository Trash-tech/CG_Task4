package com.cgvsu.math;

import org.junit.jupiter.api.Test;
import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import com.cgvsu.math.LinearAlgebra.Vector3D;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class AffineTransformerTest {
    private static final float EPSILON = 0.0001f;

        @Test
        public void testCreateScalingMatrix() {
            float sx = 2.0f, sy = 3.0f, sz = 4.0f;
            Matrix4x4 result = AffineTransformer.createScalingMatrix(sx, sy, sz);

            assertEquals(sx, result.getDataByIndexes(0, 0), EPSILON);
            assertEquals(sy, result.getDataByIndexes(1, 1), EPSILON);
            assertEquals(sz, result.getDataByIndexes(2, 2), EPSILON);
            assertEquals(1.0f, result.getDataByIndexes(3, 3), EPSILON);

            assertEquals(0.0f, result.getDataByIndexes(0, 1), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(0, 2), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(1, 0), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(1, 2), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(2, 0), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(2, 1), EPSILON);
        }

        @Test
        public void testCreateRotateXMatrix() {
            float angle = (float) Math.PI / 2;
            Matrix4x4 result = AffineTransformer.createRotateXMatrix(angle);

            assertEquals(1.0f, result.getDataByIndexes(0, 0), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(0, 1), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(0, 2), EPSILON);

            assertEquals(0.0f, result.getDataByIndexes(1, 0), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(1, 1), EPSILON, "cos(90) = 0");
            assertEquals(1.0f, result.getDataByIndexes(1, 2), EPSILON, "sin(90) = 1");

            assertEquals(0.0f, result.getDataByIndexes(2, 0), EPSILON);
            assertEquals(-1.0f, result.getDataByIndexes(2, 1), EPSILON, "-sin(90) = -1");
            assertEquals(0.0f, result.getDataByIndexes(2, 2), EPSILON, "cos(90) = 0");
        }

        @Test
        public void testCreateRotateYMatrix() {
            float angle = (float) Math.PI;
            Matrix4x4 result = AffineTransformer.createRotateYMatrix(angle);

            assertEquals(-1.0f, result.getDataByIndexes(0, 0), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(0, 1), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(0, 2), EPSILON);

            assertEquals(0.0f, result.getDataByIndexes(1, 0), EPSILON);
            assertEquals(1.0f, result.getDataByIndexes(1, 1), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(1, 2), EPSILON);

            assertEquals(0.0f, result.getDataByIndexes(2, 0), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(2, 1), EPSILON);
            assertEquals(-1.0f, result.getDataByIndexes(2, 2), EPSILON);
        }

        @Test
        public void testCreateRotateZMatrix() {
            float angle = (float) Math.PI / 4;
            Matrix4x4 result = AffineTransformer.createRotateZMatrix(angle);

            float cos45 = (float) Math.cos(angle);
            float sin45 = (float) Math.sin(angle);

            assertEquals(cos45, result.getDataByIndexes(0, 0), EPSILON);
            assertEquals(sin45, result.getDataByIndexes(0, 1), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(0, 2), EPSILON);

            assertEquals(-sin45, result.getDataByIndexes(1, 0), EPSILON);
            assertEquals(cos45, result.getDataByIndexes(1, 1), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(1, 2), EPSILON);

            assertEquals(0.0f, result.getDataByIndexes(2, 0), EPSILON);
            assertEquals(0.0f, result.getDataByIndexes(2, 1), EPSILON);
            assertEquals(1.0f, result.getDataByIndexes(2, 2), EPSILON);
        }

        @Test
        public void testCreateTranslationMatrix() {
            float tx = 5.0f, ty = 10.0f, tz = -3.0f;
            Matrix4x4 result = AffineTransformer.createTranslationMatrix(tx, ty, tz);

            assertEquals(1.0f, result.getDataByIndexes(0, 0), EPSILON);
            assertEquals(1.0f, result.getDataByIndexes(1, 1), EPSILON);
            assertEquals(1.0f, result.getDataByIndexes(2, 2), EPSILON);
            assertEquals(1.0f, result.getDataByIndexes(3, 3), EPSILON);

            assertEquals(tx, result.getDataByIndexes(0, 3), EPSILON);
            assertEquals(ty, result.getDataByIndexes(1, 3), EPSILON);
            assertEquals(tz, result.getDataByIndexes(2, 3), EPSILON);
        }

        @Test
        public void testVerticesTransformationScaling() {
            AffineTransformer transformer = new AffineTransformer();
            transformer.changeModelMatrixForScaling(2.0f, 3.0f, 4.0f);

            ArrayList<Vector3D> vertices = new ArrayList<>();
            vertices.add(new Vector3D(1.0f, 1.0f, 1.0f));
            vertices.add(new Vector3D(2.0f, 0.0f, -1.0f));

            ArrayList<Vector3D> result = transformer.verticesTransformation(
                    new ArrayList<>(vertices)
            );

            assertEquals(2.0f, result.get(0).getX(), EPSILON);
            assertEquals(3.0f, result.get(0).getY(), EPSILON);
            assertEquals(4.0f, result.get(0).getZ(), EPSILON);

            assertEquals(4.0f, result.get(1).getX(), EPSILON);
            assertEquals(0.0f, result.get(1).getY(), EPSILON);
            assertEquals(-4.0f, result.get(1).getZ(), EPSILON);
        }

        @Test
        public void testVerticesTransformationTranslation() {
            AffineTransformer transformer = new AffineTransformer();
            transformer.changeModelMatrixTranslation(5.0f, -3.0f, 2.0f);

            ArrayList<Vector3D> vertices = new ArrayList<>();
            vertices.add(new Vector3D(1.0f, 2.0f, 3.0f));

            ArrayList<Vector3D> result = transformer.verticesTransformation(
                    new ArrayList<>(vertices)
            );

            assertEquals(6.0f, result.get(0).getX(), EPSILON);
            assertEquals(-1.0f, result.get(0).getY(), EPSILON);
            assertEquals(5.0f, result.get(0).getZ(), EPSILON);
        }

        @Test
        public void testVerticesTransformationRotation() {
            AffineTransformer transformer = new AffineTransformer();
            transformer.changeModelMatrixForDegree(90.0f, 0.0f, 0.0f);

            ArrayList<Vector3D> vertices = new ArrayList<>();
            vertices.add(new Vector3D(0.0f, 1.0f, 0.0f));

            ArrayList<Vector3D> result = transformer.verticesTransformation(
                    new ArrayList<>(vertices)
            );

            assertEquals(0.0f, result.get(0).getX(), EPSILON);
            assertEquals(0.0f, result.get(0).getY(), EPSILON);
            assertEquals(-1.0f, result.get(0).getZ(), EPSILON);
        }

        @Test
        public void testNormalTransformation() {
            AffineTransformer transformer = new AffineTransformer();
            transformer.changeModelMatrixForScaling(2.0f, 2.0f, 2.0f);

            ArrayList<Vector3D> normals = new ArrayList<>();
            normals.add(new Vector3D(1.0f, 0.0f, 0.0f));
            normals.add(new Vector3D(0.0f, 1.0f, 0.0f));
            normals.add(new Vector3D(0.0f, 0.0f, 1.0f));

            ArrayList<Vector3D> result = transformer.normalTransformation(
                    new ArrayList<>(normals)
            );

            assertEquals(1.0f, result.get(0).getX(), EPSILON);
            assertEquals(0.0f, result.get(0).getY(), EPSILON);
            assertEquals(0.0f, result.get(0).getZ(), EPSILON);

            assertEquals(0.0f, result.get(1).getX(), EPSILON);
            assertEquals(1.0f, result.get(1).getY(), EPSILON);
            assertEquals(0.0f, result.get(1).getZ(), EPSILON);

            assertEquals(0.0f, result.get(2).getX(), EPSILON);
            assertEquals(0.0f, result.get(2).getY(), EPSILON);
            assertEquals(1.0f, result.get(2).getZ(), EPSILON);
        }

        @Test
        public void testComplexTransformation() {
            AffineTransformer transformer = new AffineTransformer();
            transformer.changeModelMatrixForRadians(
                    2.0f, 2.0f, 2.0f,
                    0.0f, 0.0f, 0.0f,
                    5.0f, 10.0f, 15.0f
            );

            ArrayList<Vector3D> vertices = new ArrayList<>();
            vertices.add(new Vector3D(1.0f, 2.0f, 3.0f));

            ArrayList<Vector3D> result = transformer.verticesTransformation(
                    new ArrayList<>(vertices)
            );

            assertEquals(7.0f, result.get(0).getX(), EPSILON);
            assertEquals(14.0f, result.get(0).getY(), EPSILON);
            assertEquals(21.0f, result.get(0).getZ(), EPSILON);
        }

        @Test
        public void testGettersAfterTransformation() {
            AffineTransformer transformer = new AffineTransformer();

            transformer.changeModelMatrixForRadians(
                    2.0f, 3.0f, 4.0f,
                    (float)Math.PI/2, 0.0f, 0.0f,
                    5.0f, 6.0f, 7.0f
            );

            assertEquals(2.0f, transformer.getScaleX(), EPSILON);
            assertEquals(3.0f, transformer.getScaleY(), EPSILON);
            assertEquals(4.0f, transformer.getScaleZ(), EPSILON);

            assertEquals(90.0f, transformer.getRotationXDegrees(), EPSILON);
            assertEquals(0.0f, transformer.getRotationYDegrees(), EPSILON);
            assertEquals(0.0f, transformer.getRotationZDegrees(), EPSILON);

            assertEquals(5.0f, transformer.getTranslationX(), EPSILON);
            assertEquals(6.0f, transformer.getTranslationY(), EPSILON);
            assertEquals(7.0f, transformer.getTranslationZ(), EPSILON);
        }

        @Test
        public void testDegreeRadianConversion() {
            float degrees = 180.0f;
            float radians = AffineTransformer.toRadians(degrees);
            float backToDegrees = AffineTransformer.toDegree(radians);

            assertEquals((float)Math.PI, radians, EPSILON);
            assertEquals(degrees, backToDegrees, EPSILON);

            assertEquals((float)Math.PI/2, AffineTransformer.toRadians(90.0f), EPSILON);
            assertEquals(90.0f, AffineTransformer.toDegree((float)Math.PI/2), EPSILON);
        }

        @Test
        public void testCreateModelMatrix() {
            float sx = 2.0f, sy = 3.0f, sz = 4.0f;
            float rx = (float)Math.PI/2, ry = 0.0f, rz = 0.0f;
            float tx = 5.0f, ty = 6.0f, tz = 7.0f;

            Matrix4x4 result = AffineTransformer.createModelMatrix(sx, sy, sz, rx, ry, rz, tx, ty, tz);

            assertNotNull(result);

            assertEquals(4, result.getData().length);
            assertEquals(4, result.getData()[0].length);
        }

        @Test
        public void testPartialParameterChanges() {
            AffineTransformer transformer = new AffineTransformer();

            assertEquals(1.0f, transformer.getScaleX(), EPSILON);
            assertEquals(0.0f, transformer.getTranslationX(), EPSILON);

            transformer.changeModelMatrixForScaling(2.0f, 2.0f, 2.0f);
            assertEquals(2.0f, transformer.getScaleX(), EPSILON);
            assertEquals(0.0f, transformer.getTranslationX(), EPSILON);

            transformer.changeModelMatrixTranslation(10.0f, 0.0f, 0.0f);
            assertEquals(2.0f, transformer.getScaleX(), EPSILON);
            assertEquals(10.0f, transformer.getTranslationX(), EPSILON);
        }
}
