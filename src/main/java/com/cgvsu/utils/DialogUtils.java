package com.cgvsu.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class DialogUtils {

    private static String currentStyleSheet = null;

    public static void setTheme(String cssPath) {
        currentStyleSheet = cssPath;
    }

    // Метод для вывода ошибки
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyTheme(alert);
        alert.showAndWait();
    }

    // Метод для вывода информации
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyTheme(alert);
        alert.showAndWait();
    }

    private static void applyTheme(Alert alert) {
        if (currentStyleSheet != null) {
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().clear();
            dialogPane.getStylesheets().add(currentStyleSheet);

            // Хак, чтобы иконки и цвета шрифтов обновились корректно
            dialogPane.getStyleClass().add("my-dialog");
        }
    }
}