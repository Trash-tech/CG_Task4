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
import javafx.scene.input.MouseButton;
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

    private double mousePrevX = 0;
    private double mousePrevY = 0;
    private boolean isMousePressed = false;

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

        setupMouseControls();
        setupGlobalEventListeners();

        //Выбор модели
        modelsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int index = modelsListView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < meshes.size()) {
                selectedMesh = meshes.get(index);
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
                sidebarToggle.setText("Скрыть панель"); // Если нажата, скрыть
                if (!mainSplitPane.getItems().contains(sidebar)) {
                    mainSplitPane.getItems().add(sidebar);
                    mainSplitPane.setDividerPositions(0.75);
                }
            } else {
                sidebarToggle.setText("Показать панель"); // Если отжата, показать
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
            if (!mainSplitPane.getItems().contains(sidebar)) {
                mainSplitPane.getItems().add(sidebar);
                mainSplitPane.setDividerPositions(0.75);
            }
            sidebarToggle.setText("Скрыть панель");
        } else {
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

            // Имя файла зависит от выбора
            String defaultName = saveTransformed ? "model_transformed.obj" : "model_original.obj";
            fileChooser.setInitialFileName(defaultName);

            File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
            if (file == null) {
                return;
            }

            try {
                if (saveTransformed) {
                    saveTransformedModel(selectedMesh, file.getAbsolutePath());
                    DialogUtils.showInfo("Успешно!", "Преобразованная модель успешно сохранена.");
                } else {
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
    private void onApplyTransformClick() {
        if (selectedMesh == null) {
            DialogUtils.showError("Ошибка!", "Сначала выберите модель из списка!");
            return;
        }

        try {
            float tx = Float.parseFloat(translateX.getText());
            float ty = Float.parseFloat(translateY.getText());
            float tz = Float.parseFloat(translateZ.getText());

            // Полагаем, что ввод в градусах
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
            DialogUtils.showError("Ошибка ввода", "Пожалуйста, введите корректные числа.");
        } catch (Exception e) {
            DialogUtils.showError("Ошибка", "Не удалось применить трансформацию: " + e.getMessage());
        }
    }

    @FXML
    private void onShowHelpMenuItemClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Справка");
        alert.setHeaderText("Управление и горячие клавиши");
        alert.setContentText(
                "Управление камерой:\n" +
                        "• W / S - Движение вперед / назад\n" +
                        "• A / D - Движение влево / вправо\n" +
                        "• E / Q (SPACE / SHIFT) - Движение вверх / вниз\n\n" +
                        "Работа с файлами:\n" +
                        "• Ctrl + F - Загрузить модель\n" +
                        "• Ctrl + S - Сохранить текущую модель\n\n" +
                        "Интерфейс:\n" +
                        "• Выберите модель в списке справа, чтобы редактировать её.\n"
        );

        // Применяем текущую CSS тему
        if (rootPane.getScene() != null && !rootPane.getScene().getStylesheets().isEmpty()) {
            alert.getDialogPane().getStylesheets().add(rootPane.getScene().getStylesheets().get(0));
        }

        alert.showAndWait();
    }

    private void setupGlobalEventListeners() {
        // Делаем так, чтобы canvas мог получать фокус клавиатуры
        canvas.setFocusTraversable(true);

        // При клике на canvas забираем фокус (чтобы перестать печатать в текстовых полях)
        canvas.setOnMouseClicked(e -> canvas.requestFocus());

        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    handleCameraForward(null);
                    break;
                case S:
                    handleCameraBackward(null);
                    break;
                case A:
                    handleCameraLeft(null);
                    break;
                case D:
                    handleCameraRight(null);
                    break;
                case E:
                    handleCameraUp(null);
                    break;
                case Q:
                    handleCameraDown(null);
                    break;
                case SPACE:
                    handleCameraUp(null);
                    break;
                case SHIFT:
                    handleCameraDown(null);
                    break;
            }
        });
    }

    private void setupMouseControls() {
        //При нажатие кнопки мыши запоминаем начальные координаты
        canvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                isMousePressed = true;
                mousePrevX = e.getSceneX();
                mousePrevY = e.getSceneY();
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                isMousePressed = false;
            }
        });

        //Когда двигаем мышь с зажатой кнопкой мы вращаем камеру
        canvas.setOnMouseDragged(e -> {
            if (isMousePressed) {
                double deltaX = e.getSceneX() - mousePrevX;
                double deltaY = e.getSceneY() - mousePrevY;

                camera.rotateAroundTarget((float) deltaX, (float) deltaY);

                mousePrevX = e.getSceneX();
                mousePrevY = e.getSceneY();
            }
        });

        canvas.setOnScroll(e -> {
            double delta = e.getDeltaY();
            float zoomSpeed = 2.0f; // Скорость зума

            // Вектор направления взгляда
            Vector3D forward = camera.getTarget().subtract(camera.getPosition()).normalization();

            if (delta > 0) {
                camera.movePositionWithTarget(forward.multiplyByScalar(zoomSpeed));
            } else {
                camera.movePositionWithTarget(forward.multiplyByScalar(-zoomSpeed));
            }
        });
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        Vector3D forward = camera.getTarget().subtract(camera.getPosition()).normalization();
        camera.movePositionWithTarget(forward.multiplyByScalar(TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        Vector3D backward = camera.getPosition().subtract(camera.getTarget()).normalization();
        camera.movePositionWithTarget(backward.multiplyByScalar(TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        Vector3D forward = camera.getTarget().subtract(camera.getPosition()).normalization();
        Vector3D up = new Vector3D(0, 1, 0);
        Vector3D right = forward.crossProduct(up).normalization();

        camera.movePositionWithTarget(right.multiplyByScalar(TRANSLATION));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        Vector3D forward = camera.getTarget().subtract(camera.getPosition()).normalization();
        Vector3D up = new Vector3D(0, 1, 0);
        Vector3D left = up.crossProduct(forward).normalization();

        camera.movePositionWithTarget(left.multiplyByScalar(TRANSLATION));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePositionWithTarget(new Vector3D(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePositionWithTarget(new Vector3D(0, -TRANSLATION, 0));
    }

    @FXML
    public void onResetCameraClick() {
        camera = new Camera(
                new Vector3D(0, 0, 100),
                new Vector3D(0, 0, 0),
                1.0F, 1, 0.01F, 100
        );
    }
}