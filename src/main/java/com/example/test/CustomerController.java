package com.example.test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Kontroler obsługujący widok profilu klienta.
 */
public class CustomerController {
    @FXML
    Button opcja1, opcja2, wstecz;

    /**
     * Obsługuje zdarzenie dla opcji 1.
     * Przechodzi do widoku linii.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas zmiany sceny.
     */
    public void handle_opcja1() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("line-view.fxml"));
        Stage window = (Stage) opcja1.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil pasażera");
    }

    /**
     * Obsługuje zdarzenie dla opcji 2.
     * Przechodzi do widoku wyszukiwania.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas zmiany sceny.
     */
    public void handle_opcja2() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search-view.fxml"));
        Parent root = loader.load();
        SearchController searchController = loader.getController();
        searchController.przystanki();
        Stage window = (Stage) opcja2.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil pasażera");
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku "Wstecz".
     * Przechodzi do widoku powitalnego.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas zmiany sceny.
     */
    public void handle_wstecz() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) wstecz.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 800));
        stage.setTitle("Zbiorkom");
        stage.show();
    }
}