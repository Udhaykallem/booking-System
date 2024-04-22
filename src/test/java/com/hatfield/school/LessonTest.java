package com.hatfield.school;

import com.hatfield.school.entities.Coach;
import com.hatfield.school.entities.Learner;
import com.hatfield.school.entities.Lesson;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class LessonTest {
    private int lessonMonth;
    private Lesson lesson;
    private Learner learner;
    private Coach coach;

    @Before
    public void setUp() {
        coach = new Coach("Coach Smith");
        lesson = new Lesson("Monday", lessonMonth,"4-5pm", 1, new Coach("Jessica", Arrays.asList("Monday", "Saturday"), Arrays.asList("5-6pm", "6-7pm")),4, 3);
        learner = new Learner("John Doe", 'M', 10, "123456789", 1);
    }

    @Test
    public void testAddLearner() {
        lesson.addLearner(learner);
        assertTrue(lesson.getBookedLearners().contains(learner));
    }

    @Test
    public void testRemoveLearner() {
        lesson.addLearner(learner);
        lesson.removeLearner(learner);
        assertFalse(lesson.getBookedLearners().contains(learner));
    }


}
