package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.AffineTransformer;
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
            final Model mesh,
            final int width,
            final int height)
    {
        //Matrix4x4 modelMatrix = AffineTransformer.createScalingMatrix(0.01f, 0.01f, 0.01f);
        //Matrix4x4 modelMatrix = rotateScaleTranslate();
        //Matrix4x4 modelMatrix = AffineTransformer.createTranslationMatrix(-20,0,0);

        Matrix4x4 modelMatrix = Matrix4x4.oneMatrix();
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4x4 modelViewProjectionMatrix = projectionMatrix
                .multiply(viewMatrix)
                .multiply(modelMatrix);
        /*
        System.out.println("=== RENDER DEBUG ===");

// 1. Проверим модель
        System.out.println("Model info:");
        System.out.println("  Vertices count: " + mesh.vertices.size());
        System.out.println("  Polygons count: " + mesh.polygons.size());

// 2. Проверим координаты первых вершин
        for (int i = 0; i < Math.min(5, mesh.vertices.size()); i++) {
            Vector3D v = mesh.vertices.get(i);
            System.out.println(String.format("  V[%d]: (%.2f, %.2f, %.2f)",
                    i, v.getX(), v.getY(), v.getZ()));
        }

// 3. Проверим камеру
        System.out.println("Camera info:");
        System.out.println("  Position: " + camera.getPosition());
        System.out.println("  Target: " + camera.getTarget());

// 4. Проверим матрицы
        System.out.println("Model matrix (scale 0.1):");
        System.out.println("  " + modelMatrix.toString());

        System.out.println("View matrix:");
        System.out.println("  " + viewMatrix.toString());

        System.out.println("Projection matrix:");
        System.out.println("  " + projectionMatrix.toString());

// 5. Проверим первую вершину после преобразований
        if (!mesh.vertices.isEmpty()) {
            Vector3D testVertex = mesh.vertices.get(0);
            Vector3D afterMVP = modelViewProjectionMatrix.multiplyByVector(testVertex);
            Vector2D screen = vertexToPoint(afterMVP, width, height);

            System.out.println("Test vertex transformations:");
            System.out.println("  Original: " + testVertex);
            System.out.println("  After MVP: " + afterMVP);
            System.out.println("  Screen coords: " + screen);
            System.out.println("  Screen bounds: 0,0 to " + width + "," + height);
        }

        System.out.println("=== ПОЭТАПНАЯ ПРОВЕРКА ===");

// 1. Исходная вершина
        Vector3D original = mesh.vertices.get(0);
        System.out.println("1. Исходная вершина: " + original);

// 2. После модели (масштабирование 0.1)
        Vector3D afterModel = modelMatrix.multiplyByVector(original);
        System.out.println("2. После модели (масштаб 0.1): " + afterModel);

// 3. После вида (view)
        Vector3D afterView = viewMatrix.multiplyByVector(afterModel);
        System.out.println("3. После вида: " + afterView);

// 4. После проекции
        Vector3D afterProj = projectionMatrix.multiplyByVector(afterView);
        System.out.println("4. После проекции: " + afterProj);

// 5. После полного MVP
        Vector3D afterMVP = modelViewProjectionMatrix.multiplyByVector(original);
        System.out.println("5. После MVP: " + afterMVP);

         */


        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygons.get(polygonInd).getVertexIndices().size();

            ArrayList<Vector2D> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3D vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3D vertexVecmath = new Vector3D(vertex.getX(), vertex.getY(), vertex.getZ());

                Vector2D resultPoint = vertexToPoint(modelViewProjectionMatrix.multiplyByVector(vertexVecmath), width, height);
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