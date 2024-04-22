package com.hatfield.school;

import com.hatfield.school.entities.Coach;
import com.hatfield.school.entities.Learner;
import com.hatfield.school.entities.Lesson;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class LearnerTest {

    private int lessonMonth;
    private Learner learner;
    private Lesson lesson;

    @Before
    public void setUp() {
        learner = new Learner("John Doe", 'M', 10, "123456789", 1);
        Coach coach = new Coach("Coach Smith");
        lesson = new Lesson("Monday", lessonMonth,"4-5pm", 1, new Coach("Jessica", Arrays.asList("Monday", "Saturday"), Arrays.asList("5-6pm", "6-7pm")),4, 3);
    }

    @Test
    public void testBookLesson_SuccessfulBooking() {
        learner.bookLesson(lesson);
        assertEquals(1, learner.getBookedLessons().size());
    }

    @Test
    public void testCancelLesson_SuccessfulCancellation() {
        learner.bookLesson(lesson);
        learner.cancelLesson(lesson);
        assertEquals(0, learner.getBookedLessons().size());
    }


    @Test
    public void testBookLesson_FailedBooking_InvalidLesson() {
        lesson = null; // Invalid lesson
        learner.bookLesson(lesson);
        assertEquals(0, learner.getBookedLessons().size());
    }

    @Test
    public void testBookLesson_FailedBooking_LearnerNotEligible() {
        learner = new Learner("Jane Doe", 'F', 11, "987654321", 5); // Grade 5 learner
        learner = new Learner("Jinnie", 'F', 8, "926624321", 5); // Grade 5 learner
        assertEquals(0, learner.getBookedLessons().size());
    }

    @Test
    public void testSetGradeLevel() {
        learner.setGradeLevel(3);
        assertEquals(3, learner.getGradeLevel());
    }
}
