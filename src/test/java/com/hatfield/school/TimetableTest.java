package com.hatfield.school;

import com.hatfield.school.entities.Coach;
import com.hatfield.school.entities.Learner;
import com.hatfield.school.entities.Lesson;
import com.hatfield.school.services.Timetable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {
    private int lessonMonth;
    private Timetable timetable;
    private Learner learner;
    private Lesson lesson1;

    @BeforeEach
    public void setUp() {
        timetable = new Timetable(java.time.MonthDay.now().getMonthValue());
        learner = new Learner("John", 'M', 8, "123-456-7890", 1); // Use class-level learner variable

        lesson1 = new Lesson("Monday", lessonMonth,"4-5pm", 1, new Coach("Jessica", Arrays.asList("Monday", "Saturday"), Arrays.asList("5-6pm", "6-7pm")),4, 3);

        timetable.getLessons().clear();
        timetable.getLearnerLessonsMap().clear();
        timetable.getLessons().add(lesson1);
    }

    @Test
    public void testViewTimetableByGradeLevel() {
        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(lesson1);

        assertEquals(1, expectedLessons.size(), "Expected number of lessons for Grade 1 is incorrect");

        for (Lesson lesson : expectedLessons) {
            assertTrue(timetable.getLessons().contains(lesson), "Lesson should be in the timetable for Grade 1");
        }
    }
}
