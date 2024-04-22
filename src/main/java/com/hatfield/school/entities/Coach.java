package com.hatfield.school.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Coach implements Serializable  {
    private String name;
    private List<String> availableDays;
    private List<String> availableTimeSlots;
    private static List<Coach> coaches = new ArrayList<>();
    private List<Lesson> lessons;


    // Static block to retrieve predefined coaches
    static {
        coaches.add(new Coach("Jack", Arrays.asList("Monday", "Wednesday", "Friday", "Saturday"), Arrays.asList("4-5pm", "5-6pm", "6-7pm")));
        coaches.add(new Coach("Kevin", Arrays.asList("Monday", "Wednesday", "Friday"), Arrays.asList("4-5pm", "5-6pm", "6-7pm")));
        coaches.add(new Coach("Jason", Arrays.asList("Wednesday", "Friday"), Arrays.asList("5-6pm", "6-7pm")));
        coaches.add(new Coach("Hellen", Arrays.asList("Monday", "Saturday"), Arrays.asList("5-6pm", "6-7pm")));
    }

    //constructor
    public Coach(String name, List<String> availableDays, List<String> availableTimeSlots) {
        this.name = name;
        this.availableDays = availableDays;
        this.availableTimeSlots = availableTimeSlots;
    }

    public static void addNewCoach(String name, List<String> availableDays, List<String> availableTimeSlots) {
        Coach coach = new Coach(name, availableDays, availableTimeSlots);
        getAllCoaches().add(coach);
    }

    public Coach(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable(String day) {
        return availableDays.contains(day);
    }

    public static List<Coach> getAllCoaches() {
        return coaches;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public Lesson[] getLessons() {
        return lessons.toArray(new Lesson[0]);
    }
}
