package com.cgvsu.objtool.objwriter;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ObjWriter {

    public static void writeObjToFile(Model model, String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            throw new ObjWriterException("File path is null or empty");
        }

        String content = writeObj(model);
        Files.writeString(Path.of(filePath), content, StandardCharsets.UTF_8);
    }

    public static String writeObj(Model model) {
        if (model == null) {
            throw new ObjWriterException("Model is null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Exported by ObjWriter\n");

        writeVertices(model, sb);
        if (model.getVertices() != null && !model.getVertices().isEmpty()) {
            sb.append('\n');
        }

        writeTextureVertices(model, sb);
        if (model.getTextureVertices() != null && !model.getTextureVertices().isEmpty()) {
            sb.append('\n');
        }

        writeNormals(model, sb);
        if (model.getNormals() != null && !model.getNormals().isEmpty()) {
            sb.append('\n');
        }

        writePolygons(model, sb);

        return sb.toString();
    }

    //Vertices
    private static void writeVertices(Model model, StringBuilder sb) {
        List<Vector3f> vertices = model.getVertices();
        if (vertices == null || vertices.isEmpty()) {
            sb.append("# Model has no vertices\n");
            return;
        }

        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            //validateVertex(vertex, i);
            sb.append("v ")
                    .append(formatFloatCompact(vertex.getX())).append(" ")
                    .append(formatFloatCompact(vertex.getY())).append(" ")
                    .append(formatFloatCompact(vertex.getZ())).append('\n');
        }
    }

    //Texture vertices
    private static void writeTextureVertices(Model model, StringBuilder sb) {
        List<Vector2f> textureVertices = model.getTextureVertices();
        if (textureVertices == null || textureVertices.isEmpty()) {
            sb.append("# Model has no texture vertices\n");
            return;
        }

        for (int i = 0; i < textureVertices.size(); i++) {
            Vector2f textureVertex = textureVertices.get(i);
            //validateTextureVertex(textureVertex, i)
            sb.append("vt ")
                    .append(formatFloatCompact(textureVertex.getX())).append(" ")
                    .append(formatFloatCompact(textureVertex.getY())).append('\n');
        }
    }

    //Normals
    private static void writeNormals(Model model, StringBuilder sb) {
        List<Vector3f> normals = model.getNormals();
        if (normals == null || normals.isEmpty()) return;

        for (int i = 0; i < normals.size(); i++) {
            Vector3f normal = normals.get(i);
            //validateNormal(normal, i)
            sb.append("vn ")
                    .append(formatFloatCompact(normal.getX())).append(" ")
                    .append(formatFloatCompact(normal.getY())).append(" ")
                    .append(formatFloatCompact(normal.getZ())).append('\n');
        }
    }

    //Polygons
    private static void writePolygons(Model model, StringBuilder sb) {
        List<Polygon> polygons = model.getPolygons();

        if (polygons == null || polygons.isEmpty()) {
            sb.append("# Model has no polygons\n");
            return;
        }

        for (int i = 0; i < polygons.size(); i++) {
            Polygon polygon = polygons.get(i);
            //validatePolygon(polygon, i)

            List<Integer> vertexIndices = polygon.getVertexIndices();

            if (vertexIndices == null || vertexIndices.isEmpty()) {
                sb.append("# Polygon ").append(i).append(" has no vertex indices\n");
                continue; // пропускаем невалидный полигон
            }

            List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
            List<Integer>normalIndices = polygon.getNormalIndices();

            boolean textureFlag = textureVertexIndices != null && !textureVertexIndices.isEmpty();
            boolean normalFlag = normalIndices != null && !normalIndices.isEmpty();

            sb.append("f");

            for (int j = 0; j < vertexIndices.size(); j++) {
                sb.append(" ");
                sb.append(vertexIndices.get(j) + 1);

                if (textureFlag || normalFlag) {
                    sb.append("/");

                    if (textureFlag && j < textureVertexIndices.size()) sb.append(textureVertexIndices.get(j) + 1);

                    if (normalFlag && j < normalIndices.size()) sb.append("/").append(normalIndices.get(j) + 1);
                }
            }
            sb.append("\n");
        }
    }

    private static final DecimalFormat FLOAT_FORMAT =
            new DecimalFormat("0.######", new DecimalFormatSymbols(Locale.US));

    public static String formatFloatCompact(float value) {
        if (!Float.isFinite(value)) {
            throw new ObjWriterException("Invalid float value: " + value);
        }
        synchronized (FLOAT_FORMAT) {
            return FLOAT_FORMAT.format(value);
        }
    }
}
