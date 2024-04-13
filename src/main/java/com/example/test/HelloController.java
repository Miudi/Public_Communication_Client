package com.example.test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * Kontroler obsługujący widok "hello-view.fxml", jest to ekran początkowy.
 */
public class HelloController {

    @FXML
    Button opcja1, opcja2;

    /**
     * Obsługa zdarzenia dla opcji 1.
     * Przełącza widok na "customer-view.fxml" i aktualizuje scenę na podanym etapie.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas wczytywania pliku FXML.
     */
    public void handle_opcja1() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("customer-view.fxml"));
        Stage window = (Stage) opcja1.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil pasażera");
    }

    /**
     * Obsługa zdarzenia dla opcji 2.
     * Przełącza widok na "login-view.fxml" i aktualizuje scenę na podanym etapie.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas wczytywania pliku FXML.
     */
    public void handle_opcja2() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        Stage window = (Stage) opcja2.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil kierowcy");
    }
}