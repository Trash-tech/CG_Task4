package com.cgvsu.math.LinearAlgebra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Vector4DTest {

    @Test
    public void testCreate() {
        Vector4D v = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(1.0f, v.getDataByIndex(0));
        assertEquals(2.0f, v.getDataByIndex(1));
        assertEquals(3.0f, v.getDataByIndex(2));
        assertEquals(4.0f, v.getDataByIndex(3));
    }

    @Test
    public void testIndexOutOfBoundsThrowsException() {
        Vector4D v = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        assertThrows(IllegalArgumentException.class, () -> v.getDataByIndex(5));
        assertThrows(IllegalArgumentException.class, () -> v.getDataByIndex(-1));
    }

    @Test
    public void testSum() {
        Vector4D v1 = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D v2 = new Vector4D(5.0f, 6.0f, 7.0f, 8.0f);
        Vector4D result = v1.sum(v2);
        Vector4D expected = new Vector4D(6.0f, 8.0f, 10.0f, 12.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        Vector4D v1 = new Vector4D(10.0f, 9.0f, 8.0f, 7.0f);
        Vector4D v2 = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D result = v1.subtract(v2);
        Vector4D expected = new Vector4D(9.0f, 7.0f, 5.0f, 3.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByScalar() {
        Vector4D v = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D result = v.multiplyByScalar(2.0f);
        Vector4D expected = new Vector4D(2.0f, 4.0f, 6.0f, 8.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testDivisionByScalar() {
        Vector4D v = new Vector4D(2.0f, 4.0f, 6.0f, 8.0f);
        Vector4D result = v.divisionByScalar(2.0f);
        Vector4D expected = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testDivisionByZeroThrowsException() {
        Vector4D v = new Vector4D(2.0f, 4.0f, 6.0f, 8.0f);
        assertThrows(IllegalArgumentException.class, () -> v.divisionByScalar(0.0f));
    }

    @Test
    public void testLength() {
        Vector4D v = new Vector4D(2.0f, 4.0f, 4.0f, 8.0f);
        assertEquals(10.0f, v.getLength(), 0.0001f); // sqrt(4+16+16+64) = sqrt(100) = 10
    }

    @Test
    public void testNormalization() {
        Vector4D v = new Vector4D(2.0f, 4.0f, 4.0f, 8.0f);
        Vector4D normalized = v.normalization();
        assertEquals(1.0f, normalized.getLength(), 0.0001f);
    }

    @Test
    public void testDotProduct() {
        Vector4D v1 = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D v2 = new Vector4D(5.0f, 6.0f, 7.0f, 8.0f);
        assertEquals(70.0f, v1.dotProduct(v2)); // 1*5 + 2*6 + 3*7 + 4*8 = 5+12+21+32 = 70
    }

    @Test
    public void testEquals() {
        Vector4D v1 = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D v2 = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D v3 = new Vector4D(5.0f, 6.0f, 7.0f, 8.0f);
        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
    }

    @Test
    public void testToString() {
        Vector4D v = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        assertNotNull(v.toString());
        assertTrue(v.toString().contains("1.0"));
    }
}
