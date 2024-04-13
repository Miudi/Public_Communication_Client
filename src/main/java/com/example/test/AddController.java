package com.example.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;

import java.io.IOException;

/**
 * Kontroler obsługujący widok dodawania linii.
 */
public class AddController {
    @FXML
    Button wstecz, dodaj, usun;
    @FXML
    Label atrapa;
    @FXML
    private ComboBox<String> poczatek;
    @FXML
    private ComboBox<String> koniec;
    @FXML
    private Spinner<Integer> godzina;
    @FXML
    private Spinner<Integer> minuta;
    @FXML
    private TextField Nr_linii;

    /**
     * Przypisuje listę dostępnych przystanków do listy spośród której klient wybiera przystanek początkowy i końcowy.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas nawiązywania połączenia z serwerem.
     */
    public void przystanki() throws Exception {
        JSONArray jsonArray;
        ObservableList<String> options = FXCollections.observableArrayList();
        try {
            Server_connection conn = new Server_connection();  //obiekt klasy
            jsonArray = conn.odpowiedz("unikalne", "14"); //wywołanie zapytania dla konkretnej linii

            for (int i = 0; i < jsonArray.length(); i++) {
                String przystanek = jsonArray.getJSONObject(i).getString("przystanek");
                options.add(przystanek);
            }
            poczatek.setItems(options);
            koniec.setItems(options);

            SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
            godzina.setValueFactory(hourFactory);
            SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
            minuta.setValueFactory(minuteFactory);
            hourFactory.setValue(21);
            minuteFactory.setValue(37);
        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku "Wstecz".
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas ładowania widoku "driver-view.fxml".
     */
    public void handle_wstecz() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("driver-view.fxml"));
        Stage window = (Stage) wstecz.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil kierowcy");
    }

    /**
     * Dodaje nową linię do bazy danych.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas nawiązywania połączenia z serwerem.
     */
    public void handle_dodaj() throws Exception {
        JSONArray jsonArray = null;
        try {
            Server_connection conn = new Server_connection();  //obiekt klasy
            String FromTo = poczatek.getValue() + "_" + koniec.getValue();
            String Godzina = godzina.getValue() + "_" + minuta.getValue();
            jsonArray = conn.odpowiedz("dodaj", Nr_linii.getText(), FromTo, Godzina);

        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }
        if (jsonArray.getJSONObject(0).getInt("affected_lines") > 0) {
            atrapa.setStyle("-fx-font-size: 18px;");
            atrapa.setText("Udało się dodać nową linię.");
        } else {
            atrapa.setStyle("-fx-font-size: 18px;");
            atrapa.setText("Nie udało się dodać nowej linii.");
        }
    }

    /**
     * Usuwa linię z bazy danych.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas nawiązywania połączenia z serwerem.
     */
    public void handle_usun() throws Exception {
        JSONArray jsonArray = null;
        try {
            Server_connection conn = new Server_connection();  //obiekt klasy
            jsonArray = conn.odpowiedz("usun", Nr_linii.getText(), "", "");

        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }
        if (jsonArray.getJSONObject(0).getInt("affected_lines") > 0) {
            atrapa.setStyle("-fx-font-size: 18px;");
            atrapa.setText("Udało się usunąć linię nr " + Nr_linii.getText() + ".");
        } else {
            atrapa.setStyle("-fx-font-size: 18px;");
            atrapa.setText("Nie udało się usunąć linii.");
        }
    }
}