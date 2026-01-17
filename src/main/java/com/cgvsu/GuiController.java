package com.cgvsu;

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
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.vecmath.Vector3f;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
            toggleTheme(false);
            if (!sidebarToggle.isSelected()) {
                mainSplitPane.getItems().remove(sidebar);
            }
        });
    }

    @FXML
    private void toggleSidebar(ActionEvent event) {
        if (sidebarToggle.isSelected()) {
            // Показать панель
            if (!mainSplitPane.getItems().contains(sidebar)) {
                mainSplitPane.getItems().add(sidebar);
                mainSplitPane.setDividerPositions(0.75); // 75% места для канваса
            }
        } else {
            // Скрыть панель
            mainSplitPane.getItems().remove(sidebar);
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

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");
        fileChooser.setInitialFileName("model_saved.obj");

        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return; //Когда пользователь нажал отмена
        }

        try {
            ObjWriter.writeObjToFile(mesh, file.getAbsolutePath());
            DialogUtils.showInfo("Success!", "The model has been successfully saved to a file:\n"
                    + file.getName());
        } catch (ObjWriterException e) {
            DialogUtils.showError("Model recording error", e.getMessage());
        } catch (IOException e) {
            DialogUtils.showError("File access error", "Couldn't save the file: " + e.getMessage());
        } catch (Exception e) {
            DialogUtils.showError("Unknow error", e.getMessage());
        }
    }

    @FXML public void handleCameraForward(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, 0, -TRANSLATION)); }
    @FXML public void handleCameraBackward(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, 0, TRANSLATION)); }
    @FXML public void handleCameraLeft(ActionEvent actionEvent) { camera.movePosition(new Vector3D(TRANSLATION, 0, 0)); }
    @FXML public void handleCameraRight(ActionEvent actionEvent) { camera.movePosition(new Vector3D(-TRANSLATION, 0, 0)); }
    @FXML public void handleCameraUp(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, TRANSLATION, 0)); }
    @FXML public void handleCameraDown(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, -TRANSLATION, 0)); }
}
