package com.cgvsu;

import com.cgvsu.math.AffineTransformer;
import com.cgvsu.math.LinearAlgebra.Matrix4x4;
import com.cgvsu.math.LinearAlgebra.Vector3D;
import com.cgvsu.model.Model;
import com.cgvsu.objtool.objreader.ObjReader;
import com.cgvsu.objtool.objreader.ObjReaderException;
import com.cgvsu.objtool.objwriter.ObjWriter;
import com.cgvsu.objtool.objwriter.ObjWriterException;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.utils.DialogUtils;
import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    private BorderPane rootPane; // Корневой элемент теперь BorderPane

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane canvasPane; // Панель, внутри которой лежит canvas

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private ToggleButton themeSwitch;

    @FXML
    private ToggleButton sidebarToggle;

    @FXML
    private TextField translateX, translateY, translateZ;

    @FXML
    private TextField rotateX, rotateY, rotateZ;

    @FXML
    private TextField scaleX, scaleY, scaleZ;


    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3D(0, 0, 100),
            new Vector3D(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        // Привязываем размер канваса к размеру панели, в которой он находится
        // Это позволит ему меняться при движении разделителя SplitPane
        canvasPane.widthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        canvasPane.heightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        AffineTransformer defaultTransformer = new AffineTransformer();

        scaleX.setText(String.valueOf(defaultTransformer.getScaleX()));
        scaleY.setText(String.valueOf(defaultTransformer.getScaleY()));
        scaleZ.setText(String.valueOf(defaultTransformer.getScaleZ()));

        rotateX.setText(String.valueOf(defaultTransformer.getRotationXDegrees()));
        rotateY.setText(String.valueOf(defaultTransformer.getRotationYDegrees()));
        rotateZ.setText(String.valueOf(defaultTransformer.getRotationZDegrees()));

        translateX.setText(String.valueOf(defaultTransformer.getTranslationX()));
        translateY.setText(String.valueOf(defaultTransformer.getTranslationY()));
        translateZ.setText(String.valueOf(defaultTransformer.getTranslationZ()));

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();

        // Начальная инициализация стилей
        javafx.application.Platform.runLater(() -> {
            toggleTheme(false); // Запускаем светлую тему

            // Проверяем состояние кнопки при запуске
            if (sidebarToggle.isSelected()) {
                sidebarToggle.setText("Скрыть панель"); // Если нажата - "Скрыть"
                if (!mainSplitPane.getItems().contains(sidebar)) {
                    mainSplitPane.getItems().add(sidebar);
                    mainSplitPane.setDividerPositions(0.75);
                }
            } else {
                sidebarToggle.setText("Показать панель"); // Если отжата - "Показать"
                mainSplitPane.getItems().remove(sidebar);
            }
        });
    }

    @FXML
    private void toggleSidebar(ActionEvent event) {
        if (sidebarToggle.isSelected()) {
            // Кнопка нажата
            if (!mainSplitPane.getItems().contains(sidebar)) {
                mainSplitPane.getItems().add(sidebar);
                mainSplitPane.setDividerPositions(0.75);
            }
            sidebarToggle.setText("Hide Panel");
        } else {
            // Кнопка не нажата
            mainSplitPane.getItems().remove(sidebar);
            sidebarToggle.setText("Show Panel");
        }
    }

    @FXML
    private void switchTheme(ActionEvent event) {
        toggleTheme(themeSwitch.isSelected());
        themeSwitch.setText(themeSwitch.isSelected() ? "Light Mode" : "Dark Mode");
    }

    public void toggleTheme(boolean dark) {
        Scene scene = rootPane.getScene(); // Берем сцену от rootPane
        if (scene == null) return;

        scene.getStylesheets().clear();

        String cssPath = dark ? "/com/cgvsu/fxml/style_dark.css" : "/com/cgvsu/fxml/style_light.css";
        var cssResource = getClass().getResource(cssPath);

        if (cssResource != null) {
            String cssUrl = cssResource.toExternalForm();
            scene.getStylesheets().add(cssUrl);

            DialogUtils.setTheme(cssUrl);
        }
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            DialogUtils.showInfo("Success!", "The model was successfully uploaded from the file:\n"
                    + file.getName());
        } catch (ObjReaderException e) {
            DialogUtils.showError("File reading error", e.getMessage());
        } catch (IOException e) {
            DialogUtils.showError("File access error", "The file could not be read: " + e.getMessage());
        } catch (Exception e) {
            //Непредвиденные ошибки
            DialogUtils.showError("Unknown error", e.getMessage());
        }
    }

    @FXML
    private void onSaveModelMenuItemClick() {
        if (mesh == null) {
            DialogUtils.showError("Saving error", "Upload the model first!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Сохранение модели");
        alert.setHeaderText(null);
        alert.setContentText("Вы хотите сохранить исходную модель или применить" +
                "текущие преобразования (поворот, масштаб, положение)?");

        if (rootPane.getScene() != null && !rootPane.getScene().getStylesheets().isEmpty()) {
            alert.getDialogPane().getStylesheets().add(rootPane.getScene().getStylesheets().get(0));
        }

        ButtonType buttonTypeOriginal = new ButtonType("Сохранить оригинал");
        ButtonType buttonTypeTransformed = new ButtonType("Сохранить изменённую");
        ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOriginal, buttonTypeTransformed, buttonTypeCancel);

        //Ждем выбора пользователя
        Optional<ButtonType> result = alert.showAndWait();

        // Если нажали Cancel или закрыли окно - выходим
        if (result.isEmpty() || result.get() == buttonTypeCancel) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");
        String defaultName = (result.get() == buttonTypeTransformed) ? "model_transformed.obj" : "model_original.obj";
        fileChooser.setInitialFileName(defaultName);

        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return; //Когда пользователь нажал отмена
        }

        try {
            if (result.get() == buttonTypeOriginal) {
                //Сохраняем как есть (просто передаем текущий mesh)
                ObjWriter.writeObjToFile(mesh, file.getAbsolutePath());
                DialogUtils.showInfo("Успешно!", "Оригинальная модель успешно сохранена.");
            } else {
                //Сохраняем с трансформациями (создаем новую версию и пишем её)
                saveTransformedModel(mesh, file.getAbsolutePath());
                DialogUtils.showInfo("Успешно!", "Изменённая модель успешно сохранена.");
            }
        } catch (Exception e) {
            DialogUtils.showError("Error", "Failed to save model: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //НАДО ПЕРЕНЕСТИ ЭТО НЕ ДЛЯ ГРАФИЧЕСКОГО ИНТЕРФЕЙСА
    // Метод для создания временной копии модели с примененными трансформациями
    private void saveTransformedModel(Model originalMesh, String filePath) throws IOException {
        //Получаем текущую матрицу трансформации из модели
        Matrix4x4 modelMatrix = originalMesh.getAffineTransformer().getModelMatrix();

        //Создаем временную модель
        Model tempMesh = new Model();

        // Копируем данные, которые не меняются (полигоны и текстурные координаты)
        tempMesh.setPolygons(originalMesh.getPolygons());
        tempMesh.setTextureVertices(originalMesh.getTextureVertices());

        //Пересчитываем вершины
        ArrayList<Vector3D> newVertices = new ArrayList<>();
        for (Vector3D v : originalMesh.getVertices()) {
            //Конвертируем в Vector3D для математики
            Vector3D v3d = new Vector3D(v.getX(), v.getY(), v.getZ());

            //Умножаем вершину на матрицу (M * v)
            Vector3D transformedV = modelMatrix.multiplyByVector(v3d);

            //Сохраняем результат
            newVertices.add(new Vector3D(transformedV.getX(), transformedV.getY(), transformedV.getZ()));
        }
        tempMesh.setVertices(newVertices);

        //Пересчитываем нормали, если они есть
        //Для правильного поворота нормалей нужна "Обратная Транспонированная" матрица
        if (!originalMesh.getNormals().isEmpty()) {
            ArrayList<Vector3D> newNormals = new ArrayList<>();

            //Получаем матрицу для нормалей: (M^-1)^T
            //Берем 3x3 часть (вращение и масштаб), инвертируем и транспонируем
            var normalMatrix = modelMatrix.toMatrix3x3().inverse().transpose();

            for (Vector3D n : originalMesh.getNormals()) {
                Vector3D n3d = new Vector3D(n.getX(), n.getY(), n.getZ());

                //Умножаем и обязательно нормализуем
                Vector3D transformedN = normalMatrix.multiplyByVector(n3d).normalization();

                newNormals.add(new Vector3D(transformedN.getX(), transformedN.getY(), transformedN.getZ()));
            }
            tempMesh.setNormals(newNormals);
        }

        //Отдаем эту временную модель ObjWriter
        ObjWriter.writeObjToFile(tempMesh, filePath);
    }

    @FXML
    private void onRenderButtonClick() {
        // Если модели нет, выходим
        if (mesh == null) {
            DialogUtils.showError("Error", "Load the model first!");
            return;
        }

        try {
            float tx = Float.parseFloat(translateX.getText());
            float ty = Float.parseFloat(translateY.getText());
            float tz = Float.parseFloat(translateZ.getText());

            //Полагаем, что ввод в градусах
            float rx = Float.parseFloat(rotateX.getText());
            float ry = Float.parseFloat(rotateY.getText());
            float rz = Float.parseFloat(rotateZ.getText());

            float sx = Float.parseFloat(scaleX.getText());
            float sy = Float.parseFloat(scaleY.getText());
            float sz = Float.parseFloat(scaleZ.getText());

            mesh.getAffineTransformer().changeModelMatrixForDegree(
                    sx, sy, sz,
                    rx, ry, rz,
                    tx, ty, tz
            );

        } catch (NumberFormatException e) {
            DialogUtils.showError("Input Error", "Please enter valid numbers in transformation fields.");
        } catch (Exception e) {
            DialogUtils.showError("Error", "An unexpected error occurred during rendering setup.");
        }
    }


    @FXML public void handleCameraForward(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, 0, -TRANSLATION)); }
    @FXML public void handleCameraBackward(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, 0, TRANSLATION)); }
    @FXML public void handleCameraLeft(ActionEvent actionEvent) { camera.movePosition(new Vector3D(TRANSLATION, 0, 0)); }
    @FXML public void handleCameraRight(ActionEvent actionEvent) { camera.movePosition(new Vector3D(-TRANSLATION, 0, 0)); }
    @FXML public void handleCameraUp(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, TRANSLATION, 0)); }
    @FXML public void handleCameraDown(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, -TRANSLATION, 0)); }
}
