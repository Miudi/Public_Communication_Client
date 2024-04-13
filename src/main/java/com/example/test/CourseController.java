package com.example.test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Kontroler obsługujący widok kursu linii.
 */
public class CourseController {
    @FXML
    Button wstecz;
    @FXML
    Label liniaLabel, kier1, kier2;
    @FXML
    GridPane gridPane;
    JSONArray JSONArray;

    private String linia;
    Font font_line = Font.font("Arial", FontWeight.BOLD, 35); // linia
    Font font_kier = Font.font("Arial", 25); // kierunek
    Font przystanek = Font.font("Arial", 20); // Przystanki

    /**
     * Ustawia numer linii i pobiera listę przystanków dla tej linii.
     * @param linia Numer linii.
     */
    public void setLinia(String linia) {
        this.linia = linia;
        liniaLabel.setFont(font_line);
        liniaLabel.setText("Linia: " + linia);
        try {
            Server_connection conn = new Server_connection();
            JSONArray = conn.odpowiedz("pokaz_przystanki", linia); // wywołanie zapytania dla konkretnej linii
        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }
    }

    /**
     * Usuwa niepotrzebne znaki z nazwy przystanka.
     * @param name Nazwa przystanka.
     * @return Nazwa przystanka bez niepotrzebnych znaków.
     */
    public String name_edit(String name) {
        if (name.endsWith("1") || name.endsWith("2")) {
            name = name.substring(0, name.length() - 1);
            if (name.endsWith("_")) {
                name = name.substring(0, name.length() - 1);
            }
        }
        return name;
    }

    /**
     * Ustawia nazwę kierunku i tworzy buttony dla przystanków w danym kierunku.
     * @param direction Kierunek ('kier1' lub 'kier2').
     */
    public void setKier(String direction) {

        String name;
        int columnIndex;
        if (direction.equals("kier1")) {
            name = JSONArray.getJSONObject((JSONArray.length() / 2)).getString("nazwa");
            columnIndex = 0;
        } else {
            name = JSONArray.getJSONObject(0).getString("nazwa");
            columnIndex = 1;
        }
        String kierunek = name_edit(name);

        Label kierunekLabel = new Label("Kierunek: " + kierunek);
        kierunekLabel.setFont(font_kier);
        gridPane.getChildren().add(kierunekLabel);
        GridPane.setColumnIndex(kierunekLabel, columnIndex);
        GridPane.setRowIndex(kierunekLabel, 0);

        int startIndex, endIndex;

        if (direction.equals("kier1")) {
            startIndex = 0;
            endIndex = JSONArray.length() / 2;
        } else {
            startIndex = JSONArray.length() / 2;
            endIndex = JSONArray.length();
        }

        for (int i = startIndex; i < endIndex; i++) {
            String nazwa = JSONArray.getJSONObject(i).getString("nazwa");
            nazwa = name_edit(nazwa);
            Text text = new Text(nazwa);
            text.setFont(przystanek);
            text.setUnderline(true);
            text.setFill(Color.BLUE);
            Label label = new Label(nazwa);

            final String przystanek = JSONArray.getJSONObject(i).getString("nazwa");
            String finalKierunek = kierunek;
            text.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("stop-view.fxml"));
                        Parent root = loader.load();
                        StopController stopController = loader.getController();
                        stopController.setParams(linia, przystanek, finalKierunek);
                        Stage window = (Stage) kier1.getScene().getWindow();
                        window.setScene(new Scene(root, 800, 800));
                        window.setTitle("Zbiorkom - profil kierowcy");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            gridPane.getChildren().add(text);
            GridPane.setColumnIndex(text, columnIndex);
            GridPane.setRowIndex(text, i - startIndex + 2);
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku "Wstecz".
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas zmiany sceny.
     */
    public void handle_wstecz() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("line-view.fxml"));
        Stage window = (Stage) wstecz.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil pasażera");
    }
}