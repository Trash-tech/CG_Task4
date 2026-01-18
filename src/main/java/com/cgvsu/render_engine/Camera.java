package com.cgvsu.render_engine;

import com.cgvsu.math.LinearAlgebra.Vector3D;
import com.cgvsu.math.LinearAlgebra.Matrix4x4;

public class Camera {
    private Vector3D position;
    private Vector3D target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    private float rotationYaw = 0f;
    private float rotationPitch = 0f;
    private float distance = 10f;

    public Camera(
            final Vector3D position,
            final Vector3D target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;

        Vector3D dir = position.subtract(target);
        distance = dir.getLength();

        float x = dir.getX();
        float z = dir.getZ();
        rotationYaw = (float) Math.atan2(x, z);
        rotationPitch = (float) Math.asin(dir.getY() / distance);
    }

    public void rotateAroundTarget(float deltaX, float deltaY) {
        rotationYaw += deltaX * 0.01f;
        rotationPitch += deltaY * 0.01f;

        float maxPitch = (float) (Math.PI / 2 - 0.1);
        rotationPitch = Math.max(-maxPitch, Math.min(maxPitch, rotationPitch));

        updatePosition();
    }

    private void updatePosition() {
        float x = distance * (float) Math.cos(rotationPitch) * (float) Math.sin(rotationYaw);
        float y = distance * (float) Math.sin(rotationPitch);
        float z = distance * (float) Math.cos(rotationPitch) * (float) Math.cos(rotationYaw);

        position = target.sum(new Vector3D(x, y, z));
    }

    public void setPosition(final Vector3D position) {
        this.position = position;
    }

    public void setTarget(final Vector3D target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getTarget() {
        return target;
    }

    public void movePosition(final Vector3D translation) {
        this.position = this.position.sum(translation);
        this.target = this.target.sum(translation);

        Vector3D dir = position.subtract(target);
        distance = dir.getLength();

        float x = dir.getX();
        float z = dir.getZ();
        rotationYaw = (float) Math.atan2(x, z);
        rotationPitch = (float) Math.asin(dir.getY() / distance);
    }

    public void movePositionWithTarget(Vector3D translation) {
        this.position = this.position.sum(translation);
        this.target = this.target.sum(translation);
    }

    public void moveTarget(final Vector3D translation) {
        this.target = this.target.sum(translation);
    }

    public void rotateLeft(float degrees) {
        rotateAroundTarget(-degrees * 10, 0);
    }

    public void rotateRight(float degrees) {
        rotateAroundTarget(degrees * 10, 0);
    }

    public void rotateUp(float degrees) {
        rotateAroundTarget(0, degrees * 10);
    }

    public void rotateDown(float degrees) {
        rotateAroundTarget(0, -degrees * 10);
    }

    Matrix4x4 getViewMatrix() {
        return GraphicConveyor.lookAt(position, target, new Vector3D(0, 1, 0));
    }

    Matrix4x4 getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }
}