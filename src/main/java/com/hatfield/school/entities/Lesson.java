package com.hatfield.school.entities;

import java.io.Serializable;
import java.util.*;

public class Lesson implements Serializable {
    private static int lessonCounter = 0;
    private String id;
    private String day;
    private String timeSlot;
    private int gradeLevel;
    private Coach coach;
    private int capacity; // New field to track lesson capacity
    private int rating; // New field to track lesson rating
    private boolean canceled; // New field to track lesson cancellation status
    private boolean attended; // New field to track lesson attendance status
    private int weekNumber; // New field to track the week number

    private int lessonMonth;

    private Set<Learner> bookedLearners = new HashSet<>();
    private Collection<Learner> attendedLearners;
    private List<Integer> ratings;
    private List<String> reviews;


    public Lesson(String day, int lessonMonth, String timeSlot, int gradeLevel, Coach coach, int capacity, int weekNumber) {
        this.id = generateLessonId();
        this.day = day;
        this.timeSlot = timeSlot;
        this.gradeLevel = gradeLevel;
        this.coach = coach;
        this.capacity = capacity;
        this.weekNumber = weekNumber;
        this.lessonMonth = lessonMonth;

        this.ratings = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.attendedLearners = new HashSet<>(); // Initialize attended learners collection
    }


    public String getDay() {
        return day;
    }

//    public int getMonth() {
//        return month;
//    }
//
//    public void setMonth(int month) {
//        this.month = month;
//    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getCoach() {
        return coach.getName();
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void addReview(Learner learner, String review) {
            reviews.add(review);
            reviews.add("No review provided"); // Placeholder for now, can be updated later
            System.out.println("Review submitted successfully.");
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public List<String> getReviews() {
        return reviews;
    }


    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public String getId() {
        return id;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    private String generateLessonId() {
        return String.valueOf(++lessonCounter);
    }

    // Method to book the lesson
    public void book() {
        if (capacity > 0) {
            capacity--;
        } else {
            System.out.println("No vacancy available for the lesson.");
        }
    }

    // Method to cancel the lesson
    public void cancel() {
        capacity++;
        canceled = true;
    }

    // Method to check if the lesson has vacancy
    public boolean hasVacancy() {
        return capacity > 0;
    }

    // Method to add a learner to the lesson
    public void addLearner(Learner learner) {
        if (capacity > 0) {
            // Decrease capacity
            capacity--;
            // Mark learner as attended
            markAttendance(learner);
            // Add learner to booked learners
            bookedLearners.add(learner);
        } else {
            System.out.println("No vacancy available for the lesson.");
        }
    }

    public void removeLearner(Learner learner) {
        // Increase capacity when removing a learner
        capacity++;
        // Unmark learner as attended
        learner.setAttended(false);
        // Remove learner from booked learners
        bookedLearners.remove(learner);
    }

    // Method to mark learner attendance
    public void markAttendance(Learner learner) {
        learner.setAttended(true);
    }


    // Method to get the maximum capacity of the lesson
    public int getMaxCapacity() {
        return 4; // Sample maximum capacity, replace with actual logic
    }

    // Method to get the collection of booked learners
    public Collection<Learner> getBookedLearners() {
        // Sample implementation, replace with actual logic to retrieve booked learners
        return bookedLearners;
    }

    @Override
    public String toString() {
        return "[Lesson at "+ timeSlot + " for grade level " + gradeLevel +  " by Coach " + coach.getName() + " on Week " + weekNumber + " (Capacity - " + capacity + ")]";
    }

    public int getGrade() {
        return gradeLevel;
    }


    public Collection<Learner> getAttendedLearners() {
        return attendedLearners;
    }

    public boolean isCancelled() {
        return canceled;

    }

    public int getLessonMonth() {
        return lessonMonth;
    }

}
