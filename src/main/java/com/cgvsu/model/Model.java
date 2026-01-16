package com.cgvsu.model;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Vector3f> vertices;
    private final List<Vector2f> textureVertices;
    private final List<Vector3f> normals;
    private final List<Polygon> polygons;

    public Model() {
        vertices = new ArrayList<>();
        textureVertices = new ArrayList<>();
        normals = new ArrayList<>();
        polygons = new ArrayList<>();
    }

    public List<Vector3f> getVertices() {return vertices;}

    public List<Vector2f> getTextureVertices() {return textureVertices;}

    public List<Vector3f> getNormals() {return normals;}

    public List<Polygon> getPolygons() {return polygons;}

}
