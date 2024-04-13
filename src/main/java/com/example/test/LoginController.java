package com.example.test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Kontroler obsługujący widok "login-view.fxml", umożliwia on logowanie do profilu kierowcy.
 */
public class LoginController {

    @FXML
    TextField adminUser;
    @FXML
    PasswordField adminPass;
    @FXML
    Button login, wstecz;
    @FXML
    Label blad;

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    /**
     * Obsługa zdarzenia dla przycisku "Login".
     * Sprawdza wprowadzone dane logowania i przekierowuje do odpowiedniego widoku.
     * Jeśli dane są poprawne, przełącza widok na "driver-view.fxml" i aktualizuje scenę na podanym etapie.
     * W przeciwnym razie wyświetla komunikat o błędnych danych.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas wczytywania pliku FXML.
     */
    public void handle_login() throws Exception {
        String username = adminUser.getText();
        String password = adminPass.getText();
        Server_connection conn = new Server_connection();
        if (username.equals(ADMIN_USER) && password.equals(ADMIN_PASSWORD)) {
            Parent root = FXMLLoader.load(getClass().getResource("driver-view.fxml"));
            Stage window = (Stage) login.getScene().getWindow();
            window.setScene(new Scene(root, 800, 800));
            window.setTitle("Zbiorkom - profil kierowcy");
        } else {
            blad.setText("Błędne dane!");
        }
    }

    /**
     * Obsługa zdarzenia dla przycisku "Wstecz".
     * Przełącza widok na "hello-view.fxml" i aktualizuje scenę na podanym etapie.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas wczytywania pliku FXML.
     */
    public void handle_wstecz() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage window = (Stage) wstecz.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom");
    }
}