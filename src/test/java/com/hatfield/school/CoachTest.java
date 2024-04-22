package com.hatfield.school;

import com.hatfield.school.entities.Coach;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CoachTest {

    @Test
    public void testGetName() {
        Coach coach = new Coach("Coach Smith");
        assertEquals("Coach Smith", coach.getName());
    }

    @Test
    public void testIsAvailable() {
        Coach coach = new Coach("Jack", Arrays.asList("Monday", "Wednesday", "Friday", "Saturday"), Arrays.asList("4-5pm", "5-6pm", "6-7pm"));

        assertTrue(coach.isAvailable("Monday"));
        assertTrue(coach.isAvailable("Saturday"));
        assertFalse(coach.isAvailable("Tuesday"));
    }

    // Add more test cases as needed
    
}
