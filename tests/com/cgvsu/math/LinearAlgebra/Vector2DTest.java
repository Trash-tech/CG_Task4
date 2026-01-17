package com.cgvsu.math.LinearAlgebra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {

    @Test
    public void testCreate() {
        Vector2D v = new Vector2D(1.0f, 2.0f);
        assertEquals(1.0f, v.getX());
        assertEquals(2.0f, v.getY());
    }

    @Test
    public void testEquals() {
        Vector2D v1 = new Vector2D(1.0f, 2.0f);
        Vector2D v2 = new Vector2D(1.0f, 2.0f);
        Vector2D v3 = new Vector2D(3.0f, 4.0f);
        assertTrue(v1.equals(v2));
        assertFalse(v1.equals(v3));
        assertEquals(v1, v2);
    }

    @Test
    public void testSum() {
        Vector2D v1 = new Vector2D(1.0f, 2.0f);
        Vector2D v2 = new Vector2D(3.0f, 4.0f);
        Vector2D result = v1.sum(v2);
        Vector2D expected = new Vector2D(4.0f, 6.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        Vector2D v1 = new Vector2D(5.0f, 6.0f);
        Vector2D v2 = new Vector2D(2.0f, 3.0f);
        Vector2D result = v1.subtract(v2);
        Vector2D expected = new Vector2D(3.0f, 3.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testMultiplyByScalar() {
        Vector2D v = new Vector2D(2.0f, 3.0f);
        Vector2D result = v.multiplyByScalar(2.0f);
        Vector2D expected = new Vector2D(4.0f, 6.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testDivisionByScalar() {
        Vector2D v = new Vector2D(4.0f, 6.0f);
        Vector2D result = v.divisionByScalar(2.0f);
        Vector2D expected = new Vector2D(2.0f, 3.0f);
        assertEquals(expected, result);
    }

    @Test
    public void testDivisionByZeroThrowsException() {
        Vector2D v = new Vector2D(4.0f, 6.0f);
        assertThrows(IllegalArgumentException.class, () -> v.divisionByScalar(0.0f));
    }

    @Test
    public void testLength() {
        Vector2D v = new Vector2D(3.0f, 4.0f);
        assertEquals(5.0f, v.getLength(), 0.0001f);
    }

    @Test
    public void testNormalization() {
        Vector2D v = new Vector2D(3.0f, 4.0f);
        Vector2D normalized = v.normalization();
        assertEquals(1.0f, normalized.getLength(), 0.0001f);
    }

    @Test
    public void testNormalizationZeroVectorThrowsException() {
        Vector2D v = new Vector2D(0.0f, 0.0f);
        assertThrows(IllegalArgumentException.class, () -> v.normalization());
    }

    @Test
    public void testDotProduct() {
        Vector2D v1 = new Vector2D(1.0f, 2.0f);
        Vector2D v2 = new Vector2D(3.0f, 4.0f);
        assertEquals(11.0f, v1.dotProduct(v2));
    }

    @Test
    public void testToString() {
        Vector2D v = new Vector2D(1.0f, 2.0f);
        assertNotNull(v.toString());
        assertTrue(v.toString().contains("1.0"));
    }
}