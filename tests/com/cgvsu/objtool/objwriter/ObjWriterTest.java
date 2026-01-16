package com.cgvsu.objtool.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ObjWriterTest {

    @TempDir
    Path tempDir;

    Path tempFile;

    @AfterEach
    void cleanup() throws IOException {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void testNullFilePath() {
        assertThrows(
                ObjWriterException.class,
                () -> ObjWriter.writeObjToFile(new Model(), null));
    }

    @Test
    public void testEmptyFilePath() {
        assertThrows(
                ObjWriterException.class,
                () -> ObjWriter.writeObjToFile(new Model(), ""));
    }

    @Test
    public void testNullModel() {
        assertThrows(
                ObjWriterException.class,
                () -> ObjWriter.writeObj(null));
    }


    //Model is empty
    @Test
    public void testEmptyModel() {
        Model model = new Model();

        String result = ObjWriter.writeObj(model);

        assertTrue(result.contains("# Exported by ObjWriter"));
        assertTrue(result.contains("# Model has no vertices"));
        assertTrue(result.contains("# Model has no texture vertices"));
        assertTrue(result.contains("# Model has no polygons"));
    }

    //Vertices
    @Test
    public void testVertices() {
        Model model = new Model();
        model.getVertices().addAll(List.of(
                new Vector3f(1f, 2f, 3f),
                new Vector3f(-1.5f, 0f, 0.25f)));

        String out = ObjWriter.writeObj(model);

        assertTrue(out.contains("v 1 2 3"));
        assertTrue(out.contains("v -1.5 0 0.25"));
    }

    //Texture vertices
    @Test
    public void testTextureVertices() {
        Model model = new Model();
        model.getTextureVertices().addAll(List.of(
                new Vector2f(0.1f, 0.9f),
                new Vector2f(1f, 1f)
        ));

        String out = ObjWriter.writeObj(model);

        assertTrue(out.contains("vt 0.1 0.9"));
        assertTrue(out.contains("vt 1 1"));
    }

    //Normals
    @Test
    public void testNormals() {
        Model model = new Model();
        model.getNormals().addAll(List.of(
                new Vector3f(0f, 1f, 0f),
                new Vector3f(1f, 0f, 0f)
        ));

        String out = ObjWriter.writeObj(model);

        assertTrue(out.contains("vn 0 1 0"));
        assertTrue(out.contains("vn 1 0 0"));
    }

    //formatting float
    @Test
    public void testFormatFloat() {
        assertEquals("1", ObjWriter.formatFloatCompact(1.0f));
        assertEquals("0.123456", ObjWriter.formatFloatCompact(0.123456f));
        assertEquals("-0.9999", ObjWriter.formatFloatCompact(-0.9999f));
        assertEquals("0", ObjWriter.formatFloatCompact(0f));

        //For NaN and Infinity
        Model m1 = new Model();
        m1.getVertices().add(new Vector3f(Float.NaN, 0, 0));
        assertThrows(ObjWriterException.class, () -> ObjWriter.writeObj(m1));

        Model m2 = new Model();
        m2.getVertices().add(new Vector3f(Float.POSITIVE_INFINITY, 0, 0));
        assertThrows(ObjWriterException.class, () -> ObjWriter.writeObj(m2));

        Model m3 = new Model();
        m3.getVertices().add(new Vector3f(Float.NEGATIVE_INFINITY, 0, 0));
        assertThrows(ObjWriterException.class, () -> ObjWriter.writeObj(m3));
    }

    //Polygons
    @Test
    public void testPolygons() {
        Model model = new Model();
        model.getVertices().addAll(List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0)
        ));

        //Only vertices
        Polygon onlyVertices = new Polygon();
        onlyVertices.getVertexIndices().addAll(List.of(0, 1, 2));
        model.getPolygons().add(onlyVertices);
        assertTrue(ObjWriter.writeObj(model).contains("f 1 2 3"));

        //Vertices + texture vertices
        model.getTextureVertices().addAll(List.of(
                new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(0, 1)
        ));
        Polygon v_vt = new Polygon();
        v_vt.getVertexIndices().addAll(List.of(0, 1, 2));
        v_vt.getTextureVertexIndices().addAll(List.of(0, 1, 2));
        model.getPolygons().clear();
        model.getPolygons().add(v_vt);
        assertTrue(ObjWriter.writeObj(model).contains("f 1/1 2/2 3/3"));

        //Vertices and normals
        model.getNormals().addAll(List.of(
                new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), new Vector3f(0, 0, 1)
        ));
        Polygon v_vn = new Polygon();
        v_vn.getVertexIndices().addAll(List.of(0, 1, 2));
        v_vn.getNormalIndices().addAll(List.of(0, 1, 2));
        model.getPolygons().clear();
        model.getPolygons().add(v_vn);
        assertTrue(ObjWriter.writeObj(model).contains("f 1//1 2//2 3//3"));

        //All - vertices + texture vertices + normals
        Polygon full = new Polygon();
        full.getVertexIndices().addAll(List.of(0, 1, 2));
        full.getTextureVertexIndices().addAll(List.of(0, 1, 2));
        full.getNormalIndices().addAll(List.of(0, 1, 2));
        model.getPolygons().clear();
        model.getPolygons().add(full);
        assertTrue(ObjWriter.writeObj(model).contains("f 1/1/1 2/2/2 3/3/3"));
    }

    //Different length list in polygons
    @Test
    void testPolygonsWithDiffLength() {
        Model model = new Model();
        model.getVertices().addAll(List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0)
        ));

        //Texture vertices
        model.getTextureVertices().add(new Vector2f(0, 0));
        Polygon p1 = new Polygon();
        p1.getVertexIndices().addAll(List.of(0, 1, 2));
        p1.getTextureVertexIndices().add(0);
        model.getPolygons().add(p1);
        assertDoesNotThrow(() -> ObjWriter.writeObj(model));

        //Normals
        model.getPolygons().clear();
        model.getNormals().add(new Vector3f(0, 1, 0));
        Polygon p2 = new Polygon();
        p2.getVertexIndices().addAll(List.of(0, 1, 2));
        p2.getNormalIndices().add(0);
        model.getPolygons().add(p2);
        assertDoesNotThrow(() -> ObjWriter.writeObj(model));
    }

    @Test
    void testWriteFile() throws IOException {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1, 2, 3));
        Polygon poly = new Polygon();
        poly.getVertexIndices().addAll(List.of(0, 0, 0));
        model.getPolygons().add(poly);
        tempFile = tempDir.resolve("output.obj");

        ObjWriter.writeObjToFile(model, tempFile.toString());

        assertTrue(Files.exists(tempFile));
        String content = Files.readString(tempFile, StandardCharsets.UTF_8);
        assertTrue(content.contains("v 1 2 3"));
        assertTrue(content.contains("f "));
    }

}
