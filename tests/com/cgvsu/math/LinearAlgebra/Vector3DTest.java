package com.cgvsu.math.LinearAlgebra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector3DTest {

    @Test
    public void testCreate() {
        Vector3D v = new Vector3D(1.0f, 2.0f, 3.0f);
        assertEquals(1.0f, v.getX());
        assertEquals(2.0f, v.getY());
        assertEquals(3.0f, v.getZ());
    }

    @Test
    public void testSum() {
        Vector3D v1 = new Vector3D(1.0f, 2.0f, 3.0f);
        Vector3D v2 = new Vector3D(4.0f, 5.0f, 6.0f);
        Vector3D result = v1.sum(v2);
        Vector3D expected = new Vector3D(5.0f, 7.0f, 9.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        Vector3D v1 = new Vector3D(5.0f, 6.0f, 7.0f);
        Vector3D v2 = new Vector3D(2.0f, 3.0f, 4.0f);
        Vector3D result = v1.subtract(v2);
        Vector3D expected = new Vector3D(3.0f, 3.0f, 3.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByScalar() {
        Vector3D v = new Vector3D(2.0f, 3.0f, 4.0f);
        Vector3D result = v.multiplyByScalar(2.0f);
        Vector3D expected = new Vector3D(4.0f, 6.0f, 8.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testDivisionByScalar() {
        Vector3D v = new Vector3D(4.0f, 6.0f, 8.0f);
        Vector3D result = v.divisionByScalar(2.0f);
        Vector3D expected = new Vector3D(2.0f, 3.0f, 4.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testDivisionByZeroThrowsException() {
        Vector3D v = new Vector3D(4.0f, 6.0f, 8.0f);
        assertThrows(IllegalArgumentException.class, () -> v.divisionByScalar(0.0f));
    }

    @Test
    public void testLength() {
        Vector3D v = new Vector3D(2.0f, 3.0f, 6.0f);
        assertEquals(7.0f, v.getLength(), 0.0001f);
    }

    @Test
    public void testNormalization() {
        Vector3D v = new Vector3D(2.0f, 3.0f, 6.0f);
        Vector3D normalized = v.normalization();
        assertEquals(1.0f, normalized.getLength(), 0.0001f);
    }

    @Test
    public void testNormalizationZeroVectorThrowsException() {
        Vector3D v = new Vector3D(0.0f, 0.0f, 0.0f);
        assertThrows(IllegalArgumentException.class, () -> v.normalization());
    }

    @Test
    public void testDotProduct() {
        Vector3D v1 = new Vector3D(1.0f, 2.0f, 3.0f);
        Vector3D v2 = new Vector3D(4.0f, 5.0f, 6.0f);
        assertEquals(32.0f, v1.dotProduct(v2));
    }

    @Test
    public void testCrossProduct() {
        Vector3D v1 = new Vector3D(1.0f, 0.0f, 0.0f);
        Vector3D v2 = new Vector3D(0.0f, 1.0f, 0.0f);
        Vector3D result = v1.crossProduct(v2);
        Vector3D expected = new Vector3D(0.0f, 0.0f, 1.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Vector3D v1 = new Vector3D(1.0f, 2.0f, 3.0f);
        Vector3D v2 = new Vector3D(1.0f, 2.0f, 3.0f);
        Vector3D v3 = new Vector3D(4.0f, 5.0f, 6.0f);
        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
    }

    @Test
    public void testToString() {
        Vector3D v = new Vector3D(1.0f, 2.0f, 3.0f);
        assertNotNull(v.toString());
        assertTrue(v.toString().contains("1.0"));
    }
}