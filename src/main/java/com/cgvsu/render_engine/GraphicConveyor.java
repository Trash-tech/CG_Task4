package com.cgvsu.render_engine;

import com.cgvsu.math.AffineTransformer;
import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import com.cgvsu.math.LinearAlgebra.Vector2D;
import com.cgvsu.math.LinearAlgebra.Vector3D;

public class GraphicConveyor {

    public static Matrix4x4 rotateScaleTranslate(
            float sx, float sy, float sz,
            float rx, float ry, float rz,
            float tx, float ty, float tz) {
        /*float[][] matrix = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
         */
        return AffineTransformer.createModelMatrix(sx, sy, sz, rx, ry, rz, tx, ty, tz);
    }

    public static Matrix4x4 lookAt(Vector3D eye, Vector3D target) {
        return lookAt(eye, target, new Vector3D(0F, 1.0F, 0F));
    }

    public static Matrix4x4 lookAt(Vector3D eye, Vector3D target, Vector3D up) {
        Vector3D resultZ = eye.subtract(target).normalization();
        Vector3D resultX = up.crossProduct(resultZ).normalization();
        Vector3D resultY = resultZ.crossProduct(resultX).normalization();

        float[][] matrix = new float[][]{
                {resultX.getX(), resultX.getY(), resultX.getZ(), -resultX.dotProduct(eye)},
                {resultY.getX(), resultY.getY(), resultY.getZ(), -resultY.dotProduct(eye)},
                {resultZ.getX(), resultZ.getY(), resultZ.getZ(), -resultZ.dotProduct(eye)},
                {0, 0, 0, 1}
        };
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        float f = (float) (1.0F / (Math.tan(fov * 0.5F)));
        float[][] matrix = new float[][]{
                {f / aspectRatio, 0, 0, 0},
                {0, -f, 0, 0},
                {0, 0, (farPlane + nearPlane) / (farPlane - nearPlane), 2 * nearPlane * farPlane / (nearPlane - farPlane)},
                {0, 0, 1, 0}
        };
        return new Matrix4x4(matrix);
    }
/*
    public static Vector3D multiplyMatrix4ByVector3(final Matrix4x4 matrix, final Vector3D vertex) {
        final float x = (vertex.x * matrix.m00) + (vertex.y * matrix.m10) + (vertex.z * matrix.m20) + matrix.m30;
        final float y = (vertex.x * matrix.m01) + (vertex.y * matrix.m11) + (vertex.z * matrix.m21) + matrix.m31;
        final float z = (vertex.x * matrix.m02) + (vertex.y * matrix.m12) + (vertex.z * matrix.m22) + matrix.m32;
        final float w = (vertex.x * matrix.m03) + (vertex.y * matrix.m13) + (vertex.z * matrix.m23) + matrix.m33;
        return new Vector3f(x / w, y / w, z / w);
    }

 */

    public static Vector2D vertexToPoint(final Vector3D vertex, final int width, final int height) {
        return new Vector2D(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
