package com.cgvsu.model;

import com.cgvsu.math.AffineTransformer;
import com.cgvsu.math.LinearAlgebra.Vector2D;
import com.cgvsu.math.LinearAlgebra.Vector3D;

import java.util.ArrayList;

public class Model {
    private ArrayList<Vector3D> vertices = new ArrayList<>();
    private ArrayList<Vector2D> textureVertices = new ArrayList<>();
    private ArrayList<Vector3D> normals = new ArrayList<>();
    private ArrayList<Polygon> polygons = new ArrayList<>();
    private AffineTransformer affineTransformer = new AffineTransformer();

    public AffineTransformer getAffineTransformer(){
        return affineTransformer;
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public ArrayList<Vector2D> getTextureVertices() {
        return textureVertices;
    }

    public ArrayList<Vector3D> getNormals() {
        return normals;
    }

    public ArrayList<Vector3D> getVertices() {
        return vertices;
    }

    public void setNormals(ArrayList<Vector3D> normals) {
        this.normals = normals;
    }

    public void setPolygons(ArrayList<Polygon> polygons) {
        this.polygons = polygons;
    }

    public void setTextureVertices(ArrayList<Vector2D> textureVertices) {
        this.textureVertices = textureVertices;
    }

    public void setVertices(ArrayList<Vector3D> vertices) {
        this.vertices = vertices;
    }

    public Model applyModelTransformationForSafeForOBJWriter() {
        Model model = new Model();
        model.setVertices(affineTransformer.verticesTransformation(cloneArrayList(vertices)));
        model.setNormals(affineTransformer.normalTransformation(cloneArrayList(normals)));
        model.setPolygons(polygons);
        model.setTextureVertices(textureVertices);
        return model;
    }


    public static ArrayList<Vector3D> cloneArrayList(ArrayList<Vector3D> a){
        ArrayList<Vector3D> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++){
            result.add(new Vector3D(a.get(i).getData()));
        }
        return result;
    }
}
