package com.cgvsu.math.LinearAlgebra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Matrix3x3Test {

    @Test
    public void testCreate() {
        float[][] data = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Matrix3x3 m = new Matrix3x3(data);
        assertEquals(1.0f, m.getData()[0][0]);
        assertEquals(5.0f, m.getData()[1][1]);
        assertEquals(9.0f, m.getData()[2][2]);
    }

    @Test
    public void testCreateWrongDimensionThrowsException() {
        float[][] wrongData1 = {
                {1, 2, 3},
                {4, 5, 6}
        };
        float[][] wrongData2 = {
                {1, 2},
                {3, 4},
                {5, 6}
        };
        assertThrows(IllegalArgumentException.class, () -> new Matrix3x3(wrongData1));
        assertThrows(IllegalArgumentException.class, () -> new Matrix3x3(wrongData2));
    }

    @Test
    public void testSum() {
        float[][] data1 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        float[][] data2 = {
                {9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}
        };
        float[][] expectedData = {
                {10, 10, 10},
                {10, 10, 10},
                {10, 10, 10}
        };

        Matrix3x3 m1 = new Matrix3x3(data1);
        Matrix3x3 m2 = new Matrix3x3(data2);
        Matrix3x3 result = m1.sum(m2);
        Matrix3x3 expected = new Matrix3x3(expectedData);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        float[][] data1 = {
                {5, 5, 5},
                {5, 5, 5},
                {5, 5, 5}
        };
        float[][] data2 = {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        };
        float[][] expectedData = {
                {4, 4, 4},
                {4, 4, 4},
                {4, 4, 4}
        };

        Matrix3x3 m1 = new Matrix3x3(data1);
        Matrix3x3 m2 = new Matrix3x3(data2);
        Matrix3x3 result = m1.subtract(m2);
        Matrix3x3 expected = new Matrix3x3(expectedData);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiply() {
        float[][] data1 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        float[][] data2 = {
                {9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}
        };

        Matrix3x3 m1 = new Matrix3x3(data1);
        Matrix3x3 m2 = new Matrix3x3(data2);
        Matrix3x3 result = m1.multiply(m2);

        assertEquals(30.0f, result.getData()[0][0], 0.0001f);
        assertEquals(24.0f, result.getData()[0][1], 0.0001f);
        assertEquals(84.0f, result.getData()[1][0], 0.0001f);
    }

    @Test
    public void testTranspose() {
        float[][] originalData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        float[][] expectedData = {
                {1, 4, 7},
                {2, 5, 8},
                {3, 6, 9}
        };

        Matrix3x3 original = new Matrix3x3(originalData);
        Matrix3x3 expected = new Matrix3x3(expectedData);
        Matrix3x3 result = original.transpose();
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByVector3D() {
        float[][] matrixData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Matrix3x3 m = new Matrix3x3(matrixData);
        Vector3D v = new Vector3D(1, 2, 3);
        Vector3D result = m.multiplyByVector(v);
        Vector3D expected = new Vector3D(14, 32, 50);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByVector4D() {
        float[][] matrixData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Matrix3x3 m = new Matrix3x3(matrixData);
        Vector4D v = new Vector4D(2, 4, 6, 2);
        Vector3D result = m.multiplyByVector(v);
        Vector3D expected = new Vector3D(14, 32, 50);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByVector4DWithZeroW() {
        float[][] matrixData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Matrix3x3 m = new Matrix3x3(matrixData);
        Vector4D v = new Vector4D(1, 2, 3, 0);
        Vector3D result = m.multiplyByVector(v);
        Vector3D expected = new Vector3D(14, 32, 50);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByWrongVectorDimensionThrowsException() {
        float[][] matrixData = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Matrix3x3 m = new Matrix3x3(matrixData);
        Vector2D v = new Vector2D(1, 2);
        assertThrows(IllegalArgumentException.class, () -> m.multiplyByVector(v));
    }

    @Test
    public void testZeroMatrix() {
        Matrix3x3 zero = Matrix3x3.zeroMatrix();
        Matrix3x3 expected = new Matrix3x3(new float[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        });
        assertEquals(expected, zero);
    }

    @Test
    public void testIdentityMatrix() {
        Matrix3x3 identity = Matrix3x3.oneMatrix();
        Matrix3x3 expected = new Matrix3x3(new float[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        });
        assertEquals(expected, identity);
    }

    @Test
    public void testOneMatrixMultiplication() {
        Matrix3x3 identity = Matrix3x3.oneMatrix();
        Vector3D v = new Vector3D(1, 2, 3);
        Vector3D result = identity.multiplyByVector(v);
        assertEquals(v, result);
    }

    @Test
    public void testZeroMatrixMultiplication() {
        Matrix3x3 zero = Matrix3x3.zeroMatrix();
        Vector3D v = new Vector3D(1, 2, 3);
        Vector3D result = zero.multiplyByVector(v);
        Vector3D expected = new Vector3D(0, 0, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        float[][] data = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Matrix3x3 m1 = new Matrix3x3(data);
        Matrix3x3 m2 = new Matrix3x3(data);
        Matrix3x3 m3 = new Matrix3x3(new float[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        });
        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
    }

    @Test
    public void testToString() {
        float[][] data = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        Matrix3x3 m = new Matrix3x3(data);
        String str = m.toString();
        assertNotNull(str);
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("9.0"));
    }
}