package com.example.test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONArray;

import java.io.IOException;

/**
 * Kontroler obsługujący widok profilu kierowcy.
 */
public class DriverController {

    @FXML
    Button wstecz, dodaj;

    @FXML
    private GridPane mygrid;

    /**
     * Inicjalizuje kontroler.
     * Pobiera listę dostępnych linii i tworzy przyciski dla każdej linii.
     */
    public void initialize() {
        JSONArray linie = null;
        try {
            Server_connection conn = new Server_connection();
            linie = conn.odpowiedz("pokaz_numery_linii", null);
        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }

        // Usuwamy istniejące elementy z kontenera
        mygrid.getChildren().clear();
        int row = 0;
        int column = 0;
        // Dodajemy nowe elementy do kontenera
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
     * Obsługuje zdarzenie kliknięcia przycisku "Wstecz".
     * Przechodzi do widoku głównego.
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

    /**
     * Obsługuje zdarzenie kliknięcia przycisku "Dodaj".
     * Przechodzi do widoku dodawania nowej linii.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas zmiany sceny.
     */
    public void handle_dodaj() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add-view.fxml"));
        Parent root = loader.load();
        AddController addController = loader.getController();
        addController.przystanki();

        Stage window = (Stage) dodaj.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Profil kierowcy - nowa linia");
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku linii.
     * Przechodzi do widoku ustawiania opóźnień dla wybranej linii.
     * @param event Zdarzenie akcji przycisku.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas zmiany sceny.
     */
    public void handle_wybierz(ActionEvent event) throws Exception {
        Button nrLinii = (Button) event.getSource();
        String linia = nrLinii.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("delay-view.fxml"));
        Parent root = loader.load();
        DelayController delayController = loader.getController();
        delayController.setParams(linia);
        Stage stage = (Stage) nrLinii.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 800));
        stage.setTitle("Profil kierowcy - ustawienia kursu");
        stage.show();
    }
}