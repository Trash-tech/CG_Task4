package com.cgvsu.render_engine;

import com.cgvsu.math.LinearAlgebra.Vector3D;
import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CameraTest {
    @Test
    void testCameraInitialization() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        assertEquals(position, camera.getPosition());
        assertEquals(target, camera.getTarget());
    }

    @Test
    void testSetPosition() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        Vector3D newPosition = new Vector3D(1, 2, 3);
        camera.setPosition(newPosition);

        assertEquals(newPosition, camera.getPosition());
    }

    @Test
    void testSetTarget() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        Vector3D newTarget = new Vector3D(1, 1, 1);
        camera.setTarget(newTarget);

        assertEquals(newTarget, camera.getTarget());
    }

    @Test
    void testMovePosition() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        Vector3D translation = new Vector3D(1, 1, 1);
        camera.movePosition(translation);

        assertEquals(new Vector3D(1, 1, 6), camera.getPosition());
        assertEquals(new Vector3D(1, 1, 1), camera.getTarget());
    }

    @Test
    void testMovePositionWithTarget() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        Vector3D translation = new Vector3D(1, 1, 1);
        camera.movePositionWithTarget(translation);

        assertEquals(new Vector3D(1, 1, 6), camera.getPosition());
        assertEquals(new Vector3D(1, 1, 1), camera.getTarget());
    }

    @Test
    void testMoveTarget() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        Vector3D translation = new Vector3D(1, 0, 0);
        camera.moveTarget(translation);

        assertEquals(new Vector3D(1, 0, 0), camera.getTarget());
    }

    @Test
    void testRotateAroundTarget() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        float initialY = camera.getPosition().getY();
        camera.rotateAroundTarget(0, 0.5f);

        assertNotEquals(initialY, camera.getPosition().getY());
    }

    @Test
    void testGetViewMatrix() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        Matrix4x4 viewMatrix = camera.getViewMatrix();
        assertNotNull(viewMatrix);
    }

    @Test
    void testGetProjectionMatrix() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();
        assertNotNull(projectionMatrix);
    }

    @Test
    void testSetAspectRatio() {
        Vector3D position = new Vector3D(0, 0, 5);
        Vector3D target = new Vector3D(0, 0, 0);
        Camera camera = new Camera(position, target, 60, 1.5f, 0.1f, 100);

        camera.setAspectRatio(2.0f);
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();
        assertNotNull(projectionMatrix);
    }
}