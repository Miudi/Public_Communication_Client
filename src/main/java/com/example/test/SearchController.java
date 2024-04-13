package com.example.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Kontroler obsługujący widok "search-view.fxml", klasa ta pozwala na przeszukiwanie linii autobusowych w celu znalezienia połączenia pomiędzy punktem A i B.
 */
public class SearchController {

    @FXML
    Button wstecz, szukaj;
    @FXML
    Label output;
    @FXML
    private ComboBox<String> poczatek;
    @FXML
    private ComboBox<String> koniec;
    @FXML
    private Spinner<Integer> godzina;
    @FXML
    private Spinner<Integer> minuta;

    private ObservableList<String> options;

    /**
     * Pobiera listę dostępnych przystanków i dodaje je do ComboBox.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas pobierania listy przystanków.
     */
    public void przystanki() throws Exception {
        JSONArray jsonArray = null;
        try {
            Server_connection conn = new Server_connection();  //obiekt klasy
            jsonArray = conn.odpowiedz("unikalne", "14"); //wywołanie zapytania dla konkretnej linii
        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }

        options = prepareOptions(jsonArray);

        poczatek.setItems(options);
        koniec.setItems(options);

        SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        godzina.setValueFactory(hourFactory);
        hourFactory.setValue(21);
        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        minuta.setValueFactory(minuteFactory);
        minuteFactory.setValue(37);
    }

    /**
     * Przygotowuje ObservableList przechowującą listę przystanków wydobytych z JSONArray.
     * @param jsonArray JSONArray zawierający dane przystanków.
     * @return ObservableList przechowująca listę przystanków.
     */
    public ObservableList<String> prepareOptions(JSONArray jsonArray) {
        options = FXCollections.observableArrayList();

        for (int i = 0; i < jsonArray.length(); i++) {
            String przystanek = jsonArray.getJSONObject(i).getString("przystanek");
            options.add(przystanek);
        }

        return options;
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
     * Wyszukuje linie, które przejeżdżają pomiędzy przystankiem A oraz B o określonej godzinie.
     * Obecnie ograniczone do jednego wyniku, możliwość rozwinięcia do wielu.
     * @throws Exception Wyjątek, gdy wystąpi błąd podczas wyszukiwania linii.
     */
    public void handle_szukaj() throws Exception {
        JSONArray jsonArray = null;
        try {
            Server_connection conn = new Server_connection();  //obiekt klasy
            jsonArray = conn.odpowiedz("polaczenie", String.valueOf(godzina.getValue()) + "_" + String.valueOf(minuta.getValue()), poczatek.getValue(), koniec.getValue()); //wywołanie zapytania dla konkretnej linii
            //System.out.println(godzina.getValue());
        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }

        for (int i = 0; i < 1 && i < jsonArray.length(); i++) {  //Obecnie ograniczone, ale jest możliwość wyświetlić więcej połączeń niż tylko to najwcześniejsze
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            TextFlow textFlow = convertResponse(jsonObj);
            output.setStyle("-fx-font-size: 18px;");
            output.setGraphic(textFlow);
        }
    }

    /**
     * Konwertuje otrzymaną odpowiedź z bazy danych na bardziej czytelną i opisaną formę dla użytkownika.
     * @param jsonObj JSONObject zawierający dane odpowiedzi.
     * @return TextFlow zawierający sformatowany tekst odpowiedzi.
     */
    public TextFlow convertResponse(JSONObject jsonObj) {
        int linia = jsonObj.getInt("linia_id");
        String poczatek = jsonObj.getString("przystanek_poczatkowy");
        String koniec = jsonObj.getString("przystanek_koncowy");
        int czas_start = jsonObj.getInt("czas_start");
        int czas_koniec = jsonObj.getInt("czas_koniec");
        int start_godzina = czas_start / 60;
        int start_minuta = czas_start - ((czas_start / 60) * 60);
        int koniec_godzina = czas_koniec / 60;
        int koniec_minuta = czas_koniec - ((czas_koniec / 60) * 60);
        int opoznienie = jsonObj.getInt("opoznienie");
        int czas_podrozy = jsonObj.getInt("czas_podrozy");

        // Wynik wyszukiwania
        TextFlow textFlow = new TextFlow();
        Text liniaText = new Text("Podróż linia: ");
        Text nrLiniiText = new Text("" + linia);
        Text przystanek1Text = new Text(" z przystanku: ");
        Text startPrzystanekText = new Text(poczatek);
        Text przystanek2Text = new Text(" do przystanku ");
        Text koniecPrzystanekText = new Text(koniec);
        Text odbedzieSieText = new Text("\n odbędzie się o ");
        Text startGodzinaText = new Text(start_godzina + ":" + start_minuta);
        Text zakonczyText = new Text(" i zakończy o ");
        Text koniecGodzinaText = new Text(koniec_godzina + ":" + koniec_minuta);
        Text czasPodrozy1Text = new Text("\n czas podróży to ");
        Text podrozMinutyText = new Text("" + czas_podrozy);
        Text czasPodrozy2Text = new Text(" minut, a opóźnienie wynosi ");
        Text opoznienieMinutyText = new Text("" + opoznienie);
        Text czasPodrozy3Text = new Text(" minut.");

        // Style
        Text[] elements = {nrLiniiText, startPrzystanekText, koniecPrzystanekText, startGodzinaText, koniecGodzinaText, podrozMinutyText, opoznienieMinutyText};
        String style = "-fx-font-weight: bold; -fx-underline: true; -fx-fill: blue;";

        for (Text element : elements) {
            element.setStyle(style);
        }

        textFlow.setTextAlignment(TextAlignment.CENTER);

        // Wypisanie wyniku
        textFlow.getChildren().addAll(liniaText, nrLiniiText, przystanek1Text, startPrzystanekText, przystanek2Text, koniecPrzystanekText,
                odbedzieSieText, startGodzinaText, zakonczyText, koniecGodzinaText,
                czasPodrozy1Text, podrozMinutyText, czasPodrozy2Text, opoznienieMinutyText, czasPodrozy3Text);

        return textFlow;
    }
}