package com.example.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseControllerTest {

    @Test
    public void test_name_edit() {
        CourseController courseController = new CourseController();

        // Testowanie dla nazwy kończącej się na "1"
        String name1 = "Course1";
        String expectedResult1 = "Course";
        String actualResult1 = courseController.name_edit(name1);
        assertEquals(expectedResult1, actualResult1);

        // Testowanie dla nazwy kończącej się na "2"
        String name2 = "Course2";
        String expectedResult2 = "Course";
        String actualResult2 = courseController.name_edit(name2);
        assertEquals(expectedResult2, actualResult2);

        // Testowanie dla nazwy kończącej się na "_1"
        String name3 = "Course_1";
        String expectedResult3 = "Course";
        String actualResult3 = courseController.name_edit(name3);
        assertEquals(expectedResult3, actualResult3);

        // Testowanie dla nazwy kończącej się na "_2"
        String name4 = "Course_2";
        String expectedResult4 = "Course";
        String actualResult4 = courseController.name_edit(name4);
        assertEquals(expectedResult4, actualResult4);

        // Testowanie dla nazwy, która nie wymaga zmiany
        String name5 = "Course";
        String expectedResult5 = "Course";
        String actualResult5 = courseController.name_edit(name5);
        assertEquals(expectedResult5, actualResult5);
    }
}