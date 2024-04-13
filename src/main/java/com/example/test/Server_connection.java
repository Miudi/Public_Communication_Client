package com.example.test;

import java.net.*;
import java.io.*;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Klasa obsługująca połączenie z serwerem i komunikację z nim.
 */
public class Server_connection {
    static JSONArray JSONArray2;

    /**
     * Wysyła zapytanie do serwera i odbiera odpowiedź w postaci JSONArray.
     *
     * @param type     Typ zapytania do serwera.
     * @param nr_linii Numer linii (opcjonalny).
     * @param poczatek Początkowy przystanek (opcjonalny).
     * @param koniec   Końcowy przystanek (opcjonalny).
     * @return JSONArray zawierający dane odpowiedzi.
     * @throws IOException Wyjątek, gdy wystąpi błąd podczas komunikacji z serwerem.
     */
    public JSONArray odpowiedz(String type, String nr_linii, String poczatek, String koniec) throws IOException {
        Server_connection conn = new Server_connection();
        String JSON = null;
        JSONArray jsonArray = null;
        try {
            JSON = conn.connection(type, nr_linii, poczatek, koniec);
            if (JSON.length() < 5) {
                JSON = "[{'affected_lines':" + JSON + "}]";
            }
            jsonArray = conn.convert_JSON(JSON);
            return jsonArray;
        } catch (IOException e) {
            System.err.println("Błąd podczas nawiązywania połączenia z serwerem");
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * Metoda pomocnicza dla wywołania z mniejszą ilością argumentów.
     *
     * @param type     Typ zapytania do serwera.
     * @param nr_linii Numer linii.
     * @return JSONArray zawierający dane odpowiedzi.
     * @throws IOException Wyjątek, gdy wystąpi błąd podczas komunikacji z serwerem.
     */
    public JSONArray odpowiedz(String type, String nr_linii) throws IOException {
        return odpowiedz(type, nr_linii, "", "");
    }

    /**
     * Konwertuje otrzymaną odpowiedź z serwera w formacie JSON na obiekt JSONArray.
     *
     * @param JSON Odpowiedź z serwera w formacie JSON.
     * @return JSONArray zawierający dane odpowiedzi.
     */
    public JSONArray convert_JSON(String JSON) {
        try {
            if (JSON.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(JSON);
                return jsonArray;
            } else {
                System.err.println("Invalid JSON format. JSON must start with '['");
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Nawiązuje połączenie z serwerem, wysyła zapytanie i odbiera odpowiedź.
     *
     * @param type     Typ zapytania do serwera.
     * @param nr_linii Numer linii (opcjonalny).
     * @param poczatek Początkowy przystanek (opcjonalny).
     * @param koniec   Końcowy przystanek (opcjonalny).
     * @return Odpowiedź serwera w formacie JSON.
     * @throws IOException Wyjątek, gdy wystąpi błąd podczas komunikacji z serwerem.
     */
    public String connection(String type, String nr_linii, String poczatek, String koniec) throws IOException {
        try {
            // Połączenie z serwerem + buffery wejścia i wyjścia
            Socket socket = new Socket("localhost", 5555);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String JSON = null;

            // Typy wiadomości, które chcemy obsłużyć
            if (type.equals("polaczenie")) {
                String[] parts = nr_linii.split("_");
                out.println(type + "," + poczatek + "," + koniec + "," + parts[0] + "," + parts[1]);
                JSON = in.readLine();
            } else if (type.equals("dodaj")) {
                String[] parts_adres = poczatek.split("_");
                String[] parts_time = koniec.split("_");
                out.println(type + "," + nr_linii + "," + parts_adres[0] + "," + parts_adres[1] + "," + parts_time[0] + "," + parts_time[1]);
                System.out.println(type + "," + nr_linii + "," + parts_adres[0] + "," + parts_adres[1] + "," + parts_time[0] + "," + parts_time[1]);
                JSON = in.readLine();
            } else if (type.equals("test")) {
                out.println(type);
                JSON = in.readLine();
            } else {
                out.println(type + "," + nr_linii + "," + poczatek + "," + koniec);
                JSON = in.readLine();
            }

            // Zamykanie połączenia i strumieni.
            out.close();
            in.close();
            stdIn.close();
            socket.close();
            return JSON;

            // Obsługa wyjątków
        } catch (UnknownHostException e) {
            System.err.println("Nieznany host: localhost");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Błąd wejścia/wyjścia");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda pomocnicza z mniejszą ilością argumentów.
     *
     * @param type     Typ zapytania do serwera.
     * @param nr_linii Numer linii.
     * @return Odpowiedź serwera w formacie JSON.
     * @throws IOException Wyjątek, gdy wystąpi błąd podczas komunikacji z serwerem.
     */
    public String connection(String type, String nr_linii) throws IOException {
        return connection(type, nr_linii, "", "");
    }
}