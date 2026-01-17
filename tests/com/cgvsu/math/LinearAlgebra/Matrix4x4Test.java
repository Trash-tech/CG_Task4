package com.cgvsu.math.LinearAlgebra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Matrix4x4Test {

    @Test
    public void testCreate() {
        float[][] data = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        Matrix4x4 m = new Matrix4x4(data);
        assertEquals(1.0f, m.getData()[0][0]);
        assertEquals(6.0f, m.getData()[1][1]);
        assertEquals(11.0f, m.getData()[2][2]);
        assertEquals(16.0f, m.getData()[3][3]);
    }

    @Test
    public void testCreateWrongDimensionThrowsException() {
        float[][] wrongData1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };
        float[][] wrongData2 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12}
        };
        assertThrows(IllegalArgumentException.class, () -> new Matrix4x4(wrongData1));
        assertThrows(IllegalArgumentException.class, () -> new Matrix4x4(wrongData2));
    }

    @Test
    public void testSum() {
        float[][] data1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        float[][] data2 = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };
        float[][] expectedData = {
                {17, 17, 17, 17},
                {17, 17, 17, 17},
                {17, 17, 17, 17},
                {17, 17, 17, 17}
        };

        Matrix4x4 m1 = new Matrix4x4(data1);
        Matrix4x4 m2 = new Matrix4x4(data2);
        Matrix4x4 result = m1.sum(m2);
        Matrix4x4 expected = new Matrix4x4(expectedData);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        float[][] data1 = {
                {5, 5, 5, 5},
                {5, 5, 5, 5},
                {5, 5, 5, 5},
                {5, 5, 5, 5}
        };
        float[][] data2 = {
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        };
        float[][] expectedData = {
                {4, 4, 4, 4},
                {4, 4, 4, 4},
                {4, 4, 4, 4},
                {4, 4, 4, 4}
        };

        Matrix4x4 m1 = new Matrix4x4(data1);
        Matrix4x4 m2 = new Matrix4x4(data2);
        Matrix4x4 result = m1.subtract(m2);
        Matrix4x4 expected = new Matrix4x4(expectedData);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiply() {
        float[][] data1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        float[][] data2 = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };

        Matrix4x4 m1 = new Matrix4x4(data1);
        Matrix4x4 m2 = new Matrix4x4(data2);
        Matrix4x4 result = m1.multiply(m2);

        assertEquals(80.0f, result.getData()[0][0], 0.0001f);
        assertEquals(70.0f, result.getData()[0][1], 0.0001f);
        assertEquals(240.0f, result.getData()[1][0], 0.0001f);
    }

    @Test
    public void testTranspose() {
        float[][] originalData = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        float[][] expectedData = {
                {1, 5, 9, 13},
                {2, 6, 10, 14},
                {3, 7, 11, 15},
                {4, 8, 12, 16}
        };

        Matrix4x4 original = new Matrix4x4(originalData);
        Matrix4x4 expected = new Matrix4x4(expectedData);
        Matrix4x4 result = original.transpose();
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByVector4D() {
        float[][] matrixData = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        Matrix4x4 m = new Matrix4x4(matrixData);
        Vector4D v = new Vector4D(1, 2, 3, 4);
        Vector4D result = m.multiplyByVector(v);
        Vector4D expected = new Vector4D(30, 70, 110, 150);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByVector3D() {
        float[][] matrixData = {
                {1, 0, 0, 2},
                {0, 1, 0, 3},
                {0, 0, 1, 4},
                {0, 0, 0, 1}
        };

        Matrix4x4 m = new Matrix4x4(matrixData);
        Vector3D v = new Vector3D(1, 2, 3);
        Vector3D result = m.multiplyByVector(v);
        Vector3D expected = new Vector3D(3, 5, 7);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByVector3DWithPerspective() {
        float[][] matrixData = {
                {2, 0, 0, 0},
                {0, 3, 0, 0},
                {0, 0, 4, 0},
                {0, 0, 1, 0}
        };

        Matrix4x4 m = new Matrix4x4(matrixData);
        Vector3D v = new Vector3D(1, 2, 3);
        Vector3D result = m.multiplyByVector(v);

        assertEquals(2.0f/3.0f, result.getX(), 0.0001f);
        assertEquals(2.0f, result.getY(), 0.0001f);
        assertEquals(4.0f, result.getZ(), 0.0001f);
    }

    @Test
    public void testMultiplyByVector3DWithZeroW() {
        float[][] matrixData = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 0}
        };

        Matrix4x4 m = new Matrix4x4(matrixData);
        Vector3D v = new Vector3D(1, 2, 3);
        Vector3D result = m.multiplyByVector(v);
        Vector3D expected = new Vector3D(1, 2, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByWrongVectorDimensionThrowsException() {
        float[][] matrixData = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        Matrix4x4 m = new Matrix4x4(matrixData);
        Vector2D v = new Vector2D(1, 2);
        assertThrows(IllegalArgumentException.class, () -> m.multiplyByVector(v));
    }

    @Test
    public void testZeroMatrix() {
        Matrix4x4 zero = Matrix4x4.zeroMatrix();
        Matrix4x4 expected = new Matrix4x4(new float[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        assertEquals(expected, zero);
    }

    @Test
    public void testIdentityMatrix() {
        Matrix4x4 identity = Matrix4x4.oneMatrix();
        Matrix4x4 expected = new Matrix4x4(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        assertEquals(expected, identity);
    }

    @Test
    public void testOneMatrixMultiplicationWithVector4D() {
        Matrix4x4 identity = Matrix4x4.oneMatrix();
        Vector4D v = new Vector4D(1, 2, 3, 4);
        Vector4D result = identity.multiplyByVector(v);
        assertEquals(v, result);
    }

    @Test
    public void testOneMatrixMultiplicationWithVector3D() {
        Matrix4x4 identity = Matrix4x4.oneMatrix();
        Vector3D v = new Vector3D(1, 2, 3);
        Vector3D result = identity.multiplyByVector(v);
        assertEquals(v, result);
    }

    @Test
    public void testZeroMatrixMultiplicationWithVector4D() {
        Matrix4x4 zero = Matrix4x4.zeroMatrix();
        Vector4D v = new Vector4D(1, 2, 3, 4);
        Vector4D result = zero.multiplyByVector(v);
        Vector4D expected = new Vector4D(0, 0, 0, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testZeroMatrixMultiplicationWithVector3D() {
        Matrix4x4 zero = Matrix4x4.zeroMatrix();
        Vector3D v = new Vector3D(1, 2, 3);
        Vector3D result = zero.multiplyByVector(v);
        Vector3D expected = new Vector3D(0, 0, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        float[][] data = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        Matrix4x4 m1 = new Matrix4x4(data);
        Matrix4x4 m2 = new Matrix4x4(data);
        Matrix4x4 m3 = new Matrix4x4(new float[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
    }

    @Test
    public void testToString() {
        float[][] data = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        Matrix4x4 m = new Matrix4x4(data);
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("16.0"));
    }

    @Test
    public void testMatrix4x4ToMatrix3x3(){
        Matrix4x4 testMatrix = new Matrix4x4(new float[][]{
                {0, 1, 2, 3}
                ,{4, 5, 6, 7}
                ,{8, 9, 10, 11}
                ,{12, 13, 14, 15}
        });
        Matrix3x3 m = testMatrix.toMatrix3x3();
        assertEquals(m.getData()[0][0], testMatrix.getData()[0][0]);
        assertEquals(m.getData()[0][1], testMatrix.getData()[0][1]);
        assertEquals(m.getData()[0][2], testMatrix.getData()[0][2]);
        assertEquals(m.getData()[1][0], testMatrix.getData()[1][0]);
        assertEquals(m.getData()[1][1], testMatrix.getData()[1][1]);
        assertEquals(m.getData()[1][2], testMatrix.getData()[1][2]);
        assertEquals(m.getData()[2][0], testMatrix.getData()[2][0]);
        assertEquals(m.getData()[2][1], testMatrix.getData()[2][1]);
        assertEquals(m.getData()[2][2], testMatrix.getData()[2][2]);
    }
}