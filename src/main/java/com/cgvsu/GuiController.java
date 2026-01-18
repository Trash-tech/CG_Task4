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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    private ListView<String> modelsListView;

    @FXML
    private TextField translateX, translateY, translateZ;

    @FXML
    private TextField rotateX, rotateY, rotateZ;

    @FXML
    private TextField scaleX, scaleY, scaleZ;

    private ArrayList<Model> meshes = new ArrayList<>();

    private ObservableList<String> modelNames = FXCollections.observableArrayList();

    private Model selectedMesh = null;

    private int modelCounter = 0;

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

        modelsListView.setItems(modelNames);

        //Слушатель выбора модели
        modelsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int index = modelsListView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < meshes.size()) {
                selectedMesh = meshes.get(index);
                //При выборе модели обновляем поля трансформации ее значениями
                updateTransformFields(selectedMesh);
            } else {
                selectedMesh = null;
            }
        });

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

            if (!meshes.isEmpty()) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, meshes, (int) width, (int) height);
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

    private void updateTransformFields(Model model) {
        AffineTransformer at = model.getAffineTransformer();

        translateX.setText(String.valueOf(at.getTranslationX()));
        translateY.setText(String.valueOf(at.getTranslationY()));
        translateZ.setText(String.valueOf(at.getTranslationZ()));

        rotateX.setText(String.valueOf(at.getRotationXDegrees()));
        rotateY.setText(String.valueOf(at.getRotationYDegrees()));
        rotateZ.setText(String.valueOf(at.getRotationZDegrees()));

        scaleX.setText(String.valueOf(at.getScaleX()));
        scaleY.setText(String.valueOf(at.getScaleY()));
        scaleZ.setText(String.valueOf(at.getScaleZ()));
    }

    @FXML
    private void toggleSidebar(ActionEvent event) {
        if (sidebarToggle.isSelected()) {
            // Кнопка нажата
            if (!mainSplitPane.getItems().contains(sidebar)) {
                mainSplitPane.getItems().add(sidebar);
                mainSplitPane.setDividerPositions(0.75);
            }
            sidebarToggle.setText("Скрыть панель");
        } else {
            // Кнопка не нажата
            mainSplitPane.getItems().remove(sidebar);
            sidebarToggle.setText("Показать панель");
        }
    }

    @FXML
    private void switchTheme(ActionEvent event) {
        toggleTheme(themeSwitch.isSelected());
        themeSwitch.setText(themeSwitch.isSelected() ? "Светлая тема" : "Тёмная тема");
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
        fileChooser.setTitle("Сохранение модели");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            Model currMesh = ObjReader.read(fileContent);
            meshes.add(currMesh);
            String name = file.getName() + " (" + (++modelCounter) + ")";
            modelNames.add(name);
            // Выделяем новую модель автоматически
            modelsListView.getSelectionModel().selectLast();

            DialogUtils.showInfo("Успех!", "Модель была успешно загружена из файла:\n"
                    + file.getName());
        } catch (ObjReaderException e) {
            DialogUtils.showError("Ошибка чтения файла", e.getMessage());
        } catch (IOException e) {
            DialogUtils.showError("Ошибка доступа к файлу", "Файл не удалось прочитать: " + e.getMessage());
        } catch (Exception e) {
            //Непредвиденные ошибки
            DialogUtils.showError("Неизвестная ошибка", e.getMessage());
        }
    }

    @FXML
    private void onRemoveModelClick() {
        int index = modelsListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < meshes.size()) {
            meshes.remove(index);
            modelNames.remove(index);

            if (meshes.isEmpty()) {
                selectedMesh = null;
            } else {
                modelsListView.getSelectionModel().selectLast();
            }
        }
    }

    @FXML
    private void onSaveModelMenuItemClick() {
        if (selectedMesh == null) {
            DialogUtils.showError("Ошибка сохранения", "Сначала загрузите модель!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Сохранение модели");
        dialog.setHeaderText("Настройки экспорта");

        if (rootPane.getScene() != null && !rootPane.getScene().getStylesheets().isEmpty()) {
            dialog.getDialogPane().getStylesheets().add(rootPane.getScene().getStylesheets().get(0));
        }

        Label label = new Label("Выберите режим сохранения:");
        ComboBox<String> modeComboBox = new ComboBox<>();
        modeComboBox.getItems().addAll("Исходная модель", "Трансформированная модель");
        modeComboBox.getSelectionModel().selectFirst();

        VBox content = new VBox(10);
        content.getChildren().addAll(label, modeComboBox);
        dialog.getDialogPane().setContent(content);

        // --- Создаем кнопки (ТОЛЬКО SAVE и CANCEL) ---
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().setAll(saveButtonType, cancelButtonType);

        //Ждем выбора пользователя
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == saveButtonType) {
            boolean saveTransformed = modeComboBox.getValue().equals("Трансформированная модель");

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
            fileChooser.setTitle("Сохранение модели");

            // Имя файла зависит от выбора в ComboBox
            String defaultName = saveTransformed ? "model_transformed.obj" : "model_original.obj";
            fileChooser.setInitialFileName(defaultName);

            File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
            if (file == null) {
                return;
            }

            try {
                if (saveTransformed) {
                    // Сохраняем трансформированную
                    saveTransformedModel(selectedMesh, file.getAbsolutePath());
                    DialogUtils.showInfo("Успешно!", "Преобразованная модель успешно сохранена.");
                } else {
                    // Сохраняем оригинал
                    ObjWriter.writeObjToFile(selectedMesh, file.getAbsolutePath());
                    DialogUtils.showInfo("Успешно!", "Исходная модель успешно сохранена.");
                }
            } catch (Exception e) {
                DialogUtils.showError("Ошибка!", "Не удалось сохранить модель: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void saveTransformedModel(Model originalMesh, String filePath) throws IOException {
        Model transformedMesh = originalMesh.applyModelTransformationForSafeForOBJWriter();
        ObjWriter.writeObjToFile(transformedMesh, filePath);
    }

    @FXML
    private void onRenderButtonClick() {
        // Если модели нет, выходим
        if (selectedMesh == null) {
            DialogUtils.showError("Ошибка!", "Сначала загрузите модель!");
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

            selectedMesh.getAffineTransformer().changeModelMatrixForDegree(
                    sx, sy, sz,
                    rx, ry, rz,
                    tx, ty, tz
            );

        } catch (NumberFormatException e) {
            DialogUtils.showError("Ошибка ввода", "Пожалуйста, введите числа в поля для преобразования.");
        } catch (Exception e) {
            DialogUtils.showError("Ошибка", "Во время настройки рендеринга произошла непредвиденная ошибка.");
        }
    }


    @FXML public void handleCameraForward(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, 0, -TRANSLATION)); }
    @FXML public void handleCameraBackward(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, 0, TRANSLATION)); }
    @FXML public void handleCameraLeft(ActionEvent actionEvent) { camera.movePosition(new Vector3D(TRANSLATION, 0, 0)); }
    @FXML public void handleCameraRight(ActionEvent actionEvent) { camera.movePosition(new Vector3D(-TRANSLATION, 0, 0)); }
    @FXML public void handleCameraUp(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, TRANSLATION, 0)); }
    @FXML public void handleCameraDown(ActionEvent actionEvent) { camera.movePosition(new Vector3D(0, -TRANSLATION, 0)); }
}
