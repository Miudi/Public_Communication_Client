package com.example.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class Server_connectionTest {
    String type = "type";
    String poczatek = "poczatek";
    String koniec = "koniec";
    @Test
    public void test_odpowiedz_ReturnsJSONArray() throws IOException {
        String type = "test";
        String nr_linii = "nr_linii";
        Server_connection serverConnection = new Server_connection() {
            @Override
            public String connection(String type, String nr_linii, String poczatek, String koniec) throws IOException {
                // Symulacja odpowiedzi z serwera
                return "[{\"linia_id\":\"1\"}]";
            }
        };
        JSONArray result = serverConnection.odpowiedz(type, nr_linii, poczatek, koniec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.length());
    }

    @Test
    public void test_convert_JSON_ReturnsJSONArray() {
        String validJSON = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]";
        Server_connection conn = new Server_connection();
        JSONArray result = conn.convert_JSON(validJSON);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.length());
    }
    @Test
    public void test_convert_JSON_ReturnsNull() {
        String invalidJSON = "invalid JSON";;
        Server_connection conn = new Server_connection();
        JSONArray result = conn.convert_JSON(invalidJSON);
        Assertions.assertNull(result);
    }

    @Test
    public void test_connection() throws IOException {
        Server_connection conn = new Server_connection();
        String result = conn.connection("test","","","");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("test", result);
    }

}