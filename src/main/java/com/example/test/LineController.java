package com.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONArray;

import java.io.IOException;

/**
 * Kontroler obsługujący widok "line-view.fxml", prezentuje on Linie autobusowe w postaci przycisków.
 */
public class LineController {

    JSONArray linie;
    @FXML
    Button wstecz;
    @FXML
    private GridPane mygrid;

    /**
     * Inicjalizacja kontrolera.
     * Pobiera numery linii i tworzy dla każdego numeru osobny przycisk.
     */
    public void initialize() {
        try {
            Server_connection conn = new Server_connection();
            linie = conn.odpowiedz("pokaz_numery_linii", null);
        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }

        // Usuwa istniejące elementy z kontenera
        mygrid.getChildren().clear();
        int row = 0;
        int column = 0;

        // Dodaje nowe elementy do kontenera
        for (int i = 0; i < linie.length(); i++) {
            Button button = new Button(String.valueOf(linie.getJSONObject(i).getInt("linia_id")));
            button.setMnemonicParsing(false);
            button.setFont(new Font(18.0));
            try {
                button.setOnAction(e -> {
                    try {
                        handle_wybierz(e);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mygrid.getChildren().addAll(button);
            mygrid.setRowIndex(button, row);
            GridPane.setColumnIndex(button, column);

            if (column == 5) {
                column = 0;
                row++;
            } else {
                column = column + 1;
            }
        }
    }

    /**
     * Obsługa zdarzenia dla przycisku "Wstecz".
     * Przełącza widok na "customer-view.fxml" i aktualizuje scenę na podanym etapie.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas wczytywania pliku FXML.
     */
    public void handle_wstecz() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("customer-view.fxml"));
        Stage window = (Stage) wstecz.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil pasażera");
    }

    /**
     * Obsługa zdarzenia dla wybrania przycisku linii.
     * Pobiera tekst z przycisku, tworzy widok "course-view.fxml" i przekazuje wartości do kontrolera.
     * Następnie aktualizuje scenę na podanym etapie.
     * @param event Zdarzenie naciśnięcia przycisku linii.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas wczytywania pliku FXML.
     */
    public void handle_wybierz(ActionEvent event) throws Exception {
        Button nrLinii = (Button) event.getSource();
        String linia = nrLinii.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("course-view.fxml"));
        Parent root = loader.load();
        CourseController courseController = loader.getController();
        courseController.setLinia(linia);
        courseController.setKier("kier1");
        courseController.setKier("kier2");
        Stage stage = (Stage) nrLinii.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 800));
        stage.setTitle("Zbiorkom - profil kierowcy");
        stage.show();
    }
}