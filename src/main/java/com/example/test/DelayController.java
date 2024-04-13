package com.example.test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Kontroler obsługujący widok opóźnień.
 */
public class DelayController {
    @FXML
    Button wstecz;
    @FXML
    Label liniaLabel;
    @FXML
    Label odjazd;
    @FXML
    Label opoznienie;

    private static Map<String, Integer> delayMap = new HashMap<>();

    Font font_line = Font.font("Arial", FontWeight.BOLD, 35); ///linia
    Font font_departure = Font.font("Arial", 20); //przystanek

    /**
     * Ustawia parametry początkowe tekstów.
     * @param linia nr linii autobusu.
     */
    public void setParams(String linia) {
        LocalTime czas = LocalTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        String godzina = czas.format(format);
        liniaLabel.setFont(font_line);
        liniaLabel.setText("Linia: " + linia);
        odjazd.setFont(font_departure);
        odjazd.setText("Odjazd: " + godzina);
        int i = delayMap.getOrDefault(linia, 0);
        if (i < 0) {
            opoznienie.setFont(font_departure);
            opoznienie.setText(i + " min");
            opoznienie.setStyle("-fx-text-fill: green;");
        } else if (i > 0) {
            opoznienie.setFont(font_departure);
            opoznienie.setText("+" + i + " min");
            opoznienie.setStyle("-fx-text-fill: red;");
        } else {
            opoznienie.setFont(font_departure);
            opoznienie.setText("");
            opoznienie.setStyle("");
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku "Wstecz".
     * Przechodzi do widoku profilu kierowcy.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas zmiany sceny.
     */
    public void handle_wstecz() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("driver-view.fxml"));
        Stage window = (Stage) wstecz.getScene().getWindow();
        window.setScene(new Scene(root, 800, 800));
        window.setTitle("Zbiorkom - profil kierowcy");
    }

    /**
     * Obsługuje zdarzenie ustawienia opóźnienia.
     * Wysyła zapytanie do bazy danych o ustawienie opóźnienia i aktualizuje informacje w GUI.
     * @param event Zdarzenie akcji.
     */
    public void handle_ustaw(ActionEvent event) {
        Button wartosc = (Button) event.getSource();
        String wartoscOpoznienia = wartosc.getText();
        if (wartoscOpoznienia.equals("+/- 0 min")) {
            return;
        }
        String linia = liniaLabel.getText().replace("Linia: ", "");
        int delay = delayMap.getOrDefault(linia, 0);
        if (wartoscOpoznienia.contains("-")) {
            int opoznienieInt = Integer.parseInt(wartoscOpoznienia.replace("-", "").replace(" min", ""));
            delay -= opoznienieInt;
        } else if (wartoscOpoznienia.contains("+")) {
            int opoznienieInt = Integer.parseInt(wartoscOpoznienia.replace("+", "").replace(" min", ""));
            delay += opoznienieInt;
        }
        delayMap.put(linia, delay);

        if (delay < 0) {
            opoznienie.setFont(font_departure);
            opoznienie.setText(delay + " min");
            opoznienie.setStyle("-fx-text-fill: green;");
        } else if (delay > 0) {
            // Przykładowy JSONArray
            JSONArray jsonArray = new JSONArray();
            try {
                Server_connection conn = new Server_connection();  //obiekt klasy
                LocalTime czas = LocalTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
                String godzina = czas.format(format);
                jsonArray = conn.odpowiedz("opoznienie", linia, godzina.split(":")[0], String.valueOf(delay)); //wywołanie zapytania dla konkretnej linii
            } catch (IOException e) {
                System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
                e.printStackTrace();
            }

            opoznienie.setFont(font_departure);
            opoznienie.setText("+" + delay + " min");
            opoznienie.setStyle("-fx-text-fill: red;");
        } else {
            opoznienie.setFont(font_departure);
            opoznienie.setText("+/-" + delay + " min");
            opoznienie.setStyle("");
        }
    }
}