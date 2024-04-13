package com.example.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Klasa główna aplikacji Zbiorkom.
 */
public class HelloApplication extends Application {

    /**
     * Metoda startująca aplikację.
     * Ładuje widok "hello-view.fxml" i tworzy scenę, którą wyświetla na podanym etapie.
     * @param stage Etap, na którym wyświetlana jest scena.
     * @throws IOException Wyjątek, gdy wystąpi błąd podczas wczytywania pliku FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("Zbiorkom");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metoda główna aplikacji.
     * Uruchamia aplikację.
     * @param args Argumenty wiersza poleceń.
     */
    public static void main(String[] args) {
        launch();
    }
}