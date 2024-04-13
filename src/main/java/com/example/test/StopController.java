package com.example.test;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

/**
 * Klasa kontrolera obsługująca widok przystanku, w tym godziny w których dana linia z niego odjeżdza.
 */
public class StopController {
    String line;
    @FXML
    Label liniaLabel, kierunekLabel, przystanekLabel;
    @FXML
    Button wstecz;
    @FXML
    GridPane gridPane;
    @FXML
    private VBox VBOX_ID;

    Font font_line = Font.font("Arial", FontWeight.BOLD, 35); ///linia
    Font font_stop = Font.font("Arial", 25); //przystanek
    Font font_kier = Font.font("Arial", 20); //kierunek

    TableView<Map<String, String>> tableView = new TableView<>();

    /**
     * Przypisuje wartości tekstowe do etykiet, pobiera godziny dla danej linii oraz jej przystanku.
     *
     * @param linia     Numer linii.
     * @param przystanek Nazwa przystanku.
     * @param kierunek  Kierunek podróży.
     */
    public void setParams(String linia, String przystanek, String kierunek) {
        line = linia;
        liniaLabel.setFont(font_line);
        liniaLabel.setText("Linia: " + linia);
        przystanekLabel.setFont(font_stop);
        przystanekLabel.setText("Przystanek: " + przystanek);
        kierunekLabel.setFont(font_kier);
        kierunekLabel.setText("Kierunek: " + kierunek);

        // Przykładowy JSONArray
        JSONArray jsonArray = new JSONArray();
        try {
            Server_connection conn = new Server_connection();  //obiekt klasy
            jsonArray = conn.odpowiedz("pokaz_godziny", linia, przystanek, ""); //wywolanie zapytania dla konkretnej linii
        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }
        make_table(jsonArray);
    }

    /**
     * Tworzy tabelę prezentującą godziny odjazdu linii dla danych pobranych z bazy danych.
     *
     * @param jsonArray Dane w formacie JSON.
     */
    public void make_table(JSONArray jsonArray) {
        // Liczenie wystąpień wartości czasu
        Map<Integer, List<Integer>> czasMap = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                int czas = jsonArray.getJSONObject(i).getInt("czas");
                int godzina = czas / 60;
                int minuty = czas % 60;

                if (!czasMap.containsKey(godzina)) {
                    czasMap.put(godzina, new ArrayList<>());
                }
                czasMap.get(godzina).add(minuty);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Tworzenie kolumn dla unikalnych wartości czasu
        List<Integer> uniqueCzasList = new ArrayList<>(czasMap.keySet());
        Collections.sort(uniqueCzasList);
        for (Integer godzina : uniqueCzasList) {
            TableColumn<Map<String, String>, String> column = new TableColumn<>(String.valueOf(godzina));
            column.setCellValueFactory(param -> {
                Map<String, String> rowData = param.getValue();
                return new SimpleStringProperty(rowData.getOrDefault(String.valueOf(godzina), ""));
            });
            tableView.getColumns().add(column);
        }

        // Tworzenie listy danych dla tabeli
        ObservableList<Map<String, String>> data = FXCollections.observableArrayList();
        int maxCount = Collections.max(czasMap.values(), Comparator.comparing(List::size)).size();
        for (int count = 0; count < maxCount; count++) {
            Map<String, String> rowData = new HashMap<>();
            for (Integer godzina : uniqueCzasList) {
                List<Integer> minutyList = czasMap.getOrDefault(godzina, new ArrayList<>());
                if (minutyList.size() > count) {
                    int minuty = minutyList.get(count);
                    rowData.put(String.valueOf(godzina), String.valueOf(minuty));
                } else {
                    rowData.put(String.valueOf(godzina), "");
                }
            }
            data.add(rowData);
        }

        // Dodawanie wierszy do tabeli
        tableView.setItems(data);
        tableView.getStylesheets().add("/style.css");

        VBOX_ID.getChildren().add(tableView);
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku "Wstecz".
     *
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas ładowania widoku.
     */
    public void handle_wstecz() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("course-view.fxml"));
        Parent root = loader.load();
        CourseController courseController = loader.getController();
        courseController.setLinia(line);
        courseController.setKier("kier1");
        courseController.setKier("kier2");
        Stage window = (Stage) wstecz.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil pasażera");
    }
}