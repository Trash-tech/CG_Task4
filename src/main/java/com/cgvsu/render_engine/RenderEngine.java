package com.cgvsu.render_engine;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import com.cgvsu.math.LinearAlgebra.Vector2D;
import com.cgvsu.math.LinearAlgebra.Vector3D;
import com.cgvsu.model.Model;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final ArrayList<Model> meshes,
            final int width,
            final int height) {

        for (Model mesh : meshes) {
            Matrix4x4 modelMatrix = mesh.getAffineTransformer().getModelMatrix();
            Matrix4x4 viewMatrix = camera.getViewMatrix();
            Matrix4x4 projectionMatrix = camera.getProjectionMatrix();

            Matrix4x4 modelViewProjectionMatrix = projectionMatrix
                    .multiply(viewMatrix)
                    .multiply(modelMatrix);

            final int nPolygons = mesh.getPolygons().size();
            for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
                final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();

                ArrayList<Vector2D> resultPoints = new ArrayList<>();
                for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                    Vector3D vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                    Vector3D vertexVecMath = new Vector3D(vertex.getX(), vertex.getY(), vertex.getZ());

                    Vector2D resultPoint = vertexToPoint(modelViewProjectionMatrix.multiplyByVector(vertexVecMath), width, height);
                    resultPoints.add(resultPoint);
                }

                for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                    graphicsContext.strokeLine(
                            resultPoints.get(vertexInPolygonInd - 1).getX(),
                            resultPoints.get(vertexInPolygonInd - 1).getY(),
                            resultPoints.get(vertexInPolygonInd).getX(),
                            resultPoints.get(vertexInPolygonInd).getY());
                }

                if (nVerticesInPolygon > 0)
                    graphicsContext.strokeLine(
                            resultPoints.get(nVerticesInPolygon - 1).getX(),
                            resultPoints.get(nVerticesInPolygon - 1).getY(),
                            resultPoints.get(0).getX(),
                            resultPoints.get(0).getY());
            }
        }
    }
}