package com.cgvsu.render_engine;

import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import com.cgvsu.math.LinearAlgebra.Vector2D;
import com.cgvsu.math.LinearAlgebra.Vector3D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GraphicConveyorTest {
    @Test
    void testLookAtWithUpVector() {
        Vector3D eye = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Vector3D up = new Vector3D(0, 1, 0);

        Matrix4x4 result = GraphicConveyor.lookAt(eye, target, up);
        assertNotNull(result);
    }

    @Test
    void testLookAtWithoutUpVector() {
        Vector3D eye = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);

        Matrix4x4 result = GraphicConveyor.lookAt(eye, target);
        assertNotNull(result);
    }

    @Test
    void testPerspective() {
        Matrix4x4 result = GraphicConveyor.perspective(60, 1.5f, 0.1f, 100);
        assertNotNull(result);
    }

    @Test
    void testPerspectiveDifferentValues() {
        Matrix4x4 result = GraphicConveyor.perspective(90, 2.0f, 0.5f, 200);
        assertNotNull(result);
    }

    @Test
    void testVertexToPoint() {
        Vector3D vertex = new Vector3D(0.5f, 0.5f, 0);
        Vector2D result = GraphicConveyor.vertexToPoint(vertex, 800, 600);

        assertNotNull(result);
        assertEquals(0.5f * 800 + 800 / 2.0f, result.getX(), 0.001f);
        assertEquals(-0.5f * 600 + 600 / 2.0f, result.getY(), 0.001f);
    }

    @Test
    void testVertexToPointZero() {
        Vector3D vertex = new Vector3D(0, 0, 0);
        Vector2D result = GraphicConveyor.vertexToPoint(vertex, 800, 600);

        assertEquals(800 / 2.0f, result.getX(), 0.001f);
        assertEquals(600 / 2.0f, result.getY(), 0.001f);
    }

    @Test
    void testVertexToPointNegative() {
        Vector3D vertex = new Vector3D(-0.5f, -0.5f, 0);
        Vector2D result = GraphicConveyor.vertexToPoint(vertex, 800, 600);

        assertEquals(-0.5f * 800 + 800 / 2.0f, result.getX(), 0.001f);
        assertEquals(0.5f * 600 + 600 / 2.0f, result.getY(), 0.001f);
    }
}