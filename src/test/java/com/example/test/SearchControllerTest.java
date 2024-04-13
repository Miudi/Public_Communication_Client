package com.example.test;

import javafx.collections.ObservableList;
import javafx.scene.text.TextFlow;
import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchControllerTest {

    @Test
    public void testPrepareOptions() throws Exception {
        // Tworzenie testowego JSONArray z przystankami
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("przystanek", "Przystanek 1");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("przystanek", "Przystanek 2");
        jsonArray.put(jsonObject1);
        jsonArray.put(jsonObject2);


        SearchController searchController = new SearchController();

        // Wywołanie metody prepareOptions()
        ObservableList<String> options = searchController.prepareOptions(jsonArray);

        // Sprawdzenie, czy opcje przystanków zostały poprawnie zwrócone
        assertNotNull(options);
        assertEquals(2, options.size());
        assertTrue(options.contains("Przystanek 1"));
        assertTrue(options.contains("Przystanek 2"));
    }




    @Test
    public void testConvertResponse() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("linia_id", 1);
        jsonObj.put("przystanek_poczatkowy", "A");
        jsonObj.put("przystanek_koncowy", "B");
        jsonObj.put("czas_start", 360);
        jsonObj.put("czas_koniec", 480);
        jsonObj.put("opoznienie", 15);
        jsonObj.put("czas_podrozy", 120);

        SearchController search = new SearchController();
        TextFlow result = search.convertResponse(jsonObj);
        System.out.println(result);
        assertNotNull(result);
        assertEquals(15, result.getChildren().size());
    }


}