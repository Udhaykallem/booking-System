package com.hatfield.school.services;

import com.hatfield.school.entities.Coach;
import com.hatfield.school.entities.Learner;
import com.hatfield.school.entities.Lesson;

import java.io.*;
import java.util.*;
import java.util.Random;

public class Timetable implements Serializable {
    private List<Lesson> lessons;
    private List<Learner> learners;
    private List<Coach> coaches;
    private Map<Learner, List<Lesson>> learnerLessonsMap;
    private Integer lessonMonth;
    private Map<String, List<Lesson>> dayLessonsMap;
    private Map<Integer, List<Lesson>> gradeLessonsMap;
    private Map<String, List<Lesson>> coachLessonsMap;

    public Timetable (int lessonMonth) {
        this.lessonMonth= lessonMonth;
        this.lessons = new ArrayList<>();
        this.learners = new ArrayList<>();
        this.coaches = new ArrayList<>();
        this.learnerLessonsMap = new HashMap<>();
        this.dayLessonsMap = new HashMap<>();
        this.gradeLessonsMap = new HashMap<>();
        this.coachLessonsMap = new HashMap<>();
        Map<String, Integer> coachRatingSum = new HashMap<>();
        Map<String, Integer> coachLessonCount = new HashMap<>();
        initializeLearners();
    }

    public static Timetable deserialize(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Timetable) ois.readObject();
        }
    }

    public void serialize(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    // Method to initialize predefined learners.
    private void initializeLearners() {
        // Fetch predefined learners from the Learner class
        List<Learner> predefinedLearners = Learner.getAllLearners();
        this.learners.addAll(predefinedLearners);
    }



    public void generateTimetable(int lessonMonth) {
        String[] days = {"Monday", "Wednesday", "Friday", "Saturday"};
        String[] timeSlotsMonWedFri = {"4-5pm", "5-6pm", "6-7pm"};
        String[] timeSlotsSat = {"2-3pm", "3-4pm"};

        List<Coach> availableCoaches = Coach.getAllCoaches();
        Random random = new Random(123); // Fixed seed for consistent results

        int lessonCount = 0;
        int maxLessons = 44;

        // Generate timetable for four weeks
        for (int week = 1; week <= 4; week++) {
            for (String day : days) {
                List<String> timeSlots = (day.equals("Saturday")) ? Arrays.asList(timeSlotsSat) : Arrays.asList(timeSlotsMonWedFri);
                // Keep track of assigned time slots for the current day
                Set<String> assignedTimeSlots = new HashSet<>();
                for (String timeSlot : timeSlots) {
                    // Check if the maximum number of lessons has been reached
                    if (lessonCount >= maxLessons) {
                        break;
                    }
                    // Get available coaches for the day
                    List<Coach> coachesForDay = getAvailableCoaches(day);
                    // Shuffle coaches to distribute lessons evenly
                    Collections.shuffle(coachesForDay, random); // Use the same random instance
                    // Iterate over available coaches
                    for (Coach coach : coachesForDay) {
                        // Check if the maximum number of lessons has been reached
                        if (lessonCount >= maxLessons) {
                            break;
                        }
                        // Check if the time slot is already assigned
                        if (!assignedTimeSlots.contains(timeSlot)) {
                            // Generate random grade level from 0 to 5
                            int randomGradeLevel = random.nextInt(5) + 1;
                            // Create lesson with random grade level and capacity, and the current week number
                            Lesson lesson = new Lesson(day, lessonMonth, timeSlot, randomGradeLevel, coach, 4, week);
                            // Add lesson to timetable
                            lessons.add(lesson);
                            // Increment lesson count
                            lessonCount++;
                            assignedTimeSlots.add(timeSlot);
                        }
                    }
                }
            }
        }

        System.out.println("Timetable generated successfully.");
    }

    // Method to get available coaches for a given day
    private List<Coach> getAvailableCoaches(String day) {
        List<Coach> availableCoaches = new ArrayList<>();
        for (Coach coach : Coach.getAllCoaches()) {
            if (coach.isAvailable(day)) {
                availableCoaches.add(coach);
            }
        }
        return availableCoaches;
    }

    // Method to book a lesson for a learner
    public boolean bookLesson(Learner learner, Lesson lesson) {
        // Check if the learner is already booked for the lesson
        if (learnerLessonsMap.containsKey(learner) && learnerLessonsMap.get(learner).contains(lesson)) {
            System.out.println(learner.getName() + " is already booked for this lesson.");
            return false;
        }

        // Check if the learner can book the lesson and there is vacancy
        if (canBookLesson(learner, lesson) && lesson.hasVacancy()) {
            lesson.book(); // Decrement lesson capacity by 1
            learnerLessonsMap.computeIfAbsent(learner, k -> new ArrayList<>()).add(lesson);
            return true;
        } else {
            System.out.println("Cannot book the lesson for " + learner.getName() + ". Invalid grade level or no vacancy.");
            return false;
        }
    }


    // Method to cancel a booked lesson for a learner
    public boolean cancelLesson(Learner learner, Lesson lesson) {
        if (learnerLessonsMap.containsKey(learner) && learnerLessonsMap.get(learner).contains(lesson)) {
            // Check if the lesson has been attended
            if (getLearnerAttendedLessons(learner).contains(lesson)) {
                System.out.println("Cannot cancel the attended lesson.");
                return false; // Cancellation failed
            }

            lesson.cancel(); // Cancel the lesson
            List<Lesson> learnerLessons = learnerLessonsMap.get(learner);
            // Update learner's lesson map if needed
//        learnerLessonsMap.put(learner, learnerLessons);

            // Add the cancelled lesson to the cancelled lessons map
            List<Lesson> cancelledLessons = getLearnerCancelledLessons(learner);
            cancelledLessons.add(lesson); // Add to cancelled lessons
            System.out.println("Added to CancelledLessonMap ");

            System.out.println("Lesson cancelled successfully for " + learner.getName());
            return true; // Cancellation successful
        } else {
            System.out.println("No booked lesson found for " + learner.getName() + " matching the specified lesson.");
            return false; // Cancellation failed
        }
    }


    public boolean attendLesson(Learner learner, Lesson lesson) {
        // Check if the lesson is in the learner's booked lessons
        if (learnerLessonsMap.containsKey(learner) && learnerLessonsMap.get(learner).contains(lesson)) {
            // Check if the lesson has been cancelled
            if (getLearnerCancelledLessons(learner).contains(lesson)) {
                System.out.println("Cannot attend the cancelled lesson.");
                return false; // Attendance failed
            }

            // Mark the lesson as attended
            lesson.setAttended(true);
            updateGradeLevel(learner, lesson);
            System.out.println(learner.getName() + " attended the lesson successfully.");

            List<Lesson> attendedLessons = getLearnerAttendedLessons(learner);
            attendedLessons.add(lesson); // Add to attended lessons
            return true; // Attendance successful
        } else {
            System.out.println(learner.getName() + " has not booked the specified lesson.");
            return false; // Attendance failed
        }
    }



    // Method to update learner's grade level after attending a lesson
    public void updateGradeLevel(Learner learner, Lesson lesson) {
        // Implement grade level update logic here
        int currentGradeLevel = learner.getGradeLevel();
        if (lesson.getGradeLevel() == currentGradeLevel || lesson.getGradeLevel() == currentGradeLevel + 1) {
            learner.setGradeLevel(lesson.getGradeLevel());
            System.out.println(learner.getName() + "'s grade level updated to " + lesson.getGradeLevel());
        } else {
            System.out.println("Cannot update " + learner.getName() + "'s grade level. Invalid lesson grade level.");
        }
    }

    // Method to submit review and rating for a lesson
    public void submitRating(Lesson lesson, int rating) {
        // Implement review submission logic here
        if (rating >= 1 && rating <= 5) {
            lesson.setRating(rating);
            System.out.println("Rating submitted successfully for lesson: " + lesson.getId());
        } else {
            System.out.println("Invalid rating. Rating should be between 1 and 5.");
        }
    }

    public void addReview(Lesson lesson, Learner learner, String review) {
        // Check if the lesson is in the learner's booked lessons
        if (learnerLessonsMap.containsKey(learner) && learnerLessonsMap.get(learner).contains(lesson)) {
            // Add the review to the lesson
            lesson.addReview(learner, review);
            System.out.println("Review added successfully for lesson: " + lesson.getId());
        } else {
            System.out.println(learner.getName() + " has not booked the specified lesson.");
        }
    }

    public List<Lesson> getLearnerLessons(Learner learner, int month) {
        List<Lesson> learnerLessons = new ArrayList<>();
        for (Lesson lesson : learnerLessonsMap.getOrDefault(learner, Collections.emptyList())) {
            if (lesson.getLessonMonth() == month) {
                learnerLessons.add(lesson);
            }
        }
        return learnerLessons;
    }

    public List<Lesson> getLearnerLessons(Learner learner) {
        return learnerLessonsMap.getOrDefault(learner, Collections.emptyList());
    }

    public List<Lesson> getLearnerAttendedLessons(Learner learner, int month) {
        List<Lesson> attendedLessons = new ArrayList<>();
        for (Lesson lesson : getLearnerLessons(learner, month)) {
            if (lesson.isAttended()) {
                attendedLessons.add(lesson);
            }
        }
        return attendedLessons;
    }

    public List<Lesson> getLearnerAttendedLessons(Learner learner) {
        List<Lesson> attendedLessons = new ArrayList<>();
        for (Lesson lesson : getLearnerLessons(learner)) {
            if (lesson.isAttended()) {
                attendedLessons.add(lesson);
            }
        }
        return attendedLessons;
    }

    public List<Lesson> getLearnerCancelledLessons(Learner learner, int month) {
        List<Lesson> cancelledLessons = new ArrayList<>();
        for (Lesson lesson : getLearnerLessons(learner, month)) {
            if (lesson.isCancelled()) {
                cancelledLessons.add(lesson);
            }
        }
        return cancelledLessons;
    }

    public List<Lesson> getLearnerCancelledLessons(Learner learner) {
        List<Lesson> cancelledLessons = new ArrayList<>();
        for (Lesson lesson : getLearnerLessons(learner)) {
            if (lesson.isCancelled()) {
                cancelledLessons.add(lesson);
            }
        }
        return cancelledLessons;
    }

    // Helper method to check if a learner can book a lesson
    private boolean canBookLesson(Learner learner, Lesson lesson) {
        return (learner.getGradeLevel() == lesson.getGradeLevel() || learner.getGradeLevel() == lesson.getGradeLevel() - 1); // Assuming 44 lessons for 4 weeks
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public Map<Learner, List<Lesson>> getLearnerLessonsMap() {
        return learnerLessonsMap;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Lesson lesson : lessons) {
            sb.append(lesson).append("\n");
        }
        return sb.toString();
    }

    public Learner[] getLearners() {
        return learners.toArray(new Learner[0]);
    }

    public List<Lesson> getLessonsByDay(String day) {
        List<Lesson> lessonsByDay = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getDay().equalsIgnoreCase(day)) {
                lessonsByDay.add(lesson);
            }
        }
        return lessonsByDay;
    }

    public List<Lesson> getLessonsByGradeLevel(int gradeLevel) {
        List<Lesson> lessonsByGradeLevel = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getGradeLevel() == gradeLevel) {
                lessonsByGradeLevel.add(lesson);
            }
        }
        return lessonsByGradeLevel;
    }

    public List<Lesson> getLessonsByCoach(String coachName) {
        List<Lesson> lessonsByCoach = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getCoach().equalsIgnoreCase(coachName)) {
                lessonsByCoach.add(lesson);
            }
        }
        return lessonsByCoach;
    }

    public Lesson getLessonById(String lessonId) {
        for (Lesson lesson : lessons) {
            if (lesson.getId().equals(lessonId)) {
                return lesson;
            }
        }
        return null; // If no lesson with the given ID is found
    }

    public List<Lesson> getLearnerLessonsForMonth(Learner learner, int month) {
        List<Lesson> learnerLessonsForMonth = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getLessonMonth() == month && learnerLessonsMap.containsKey(learner) && learnerLessonsMap.get(learner).contains(lesson)) {
                learnerLessonsForMonth.add(lesson);
            }
        }
        return learnerLessonsForMonth;
    }

    public void fetchLearnerReport(Learner learner, int month) {
        // Get the learner's booked lessons
        List<Lesson> bookedLessons = getLearnerLessons(learner, month);
        List<Lesson> cancelledLesson = getLearnerCancelledLessons(learner, month);
        List<Lesson> attendedLessons = getLearnerAttendedLessons(learner);

        // Count the number of booked, cancelled, and attended lessons
        int bookedCount = bookedLessons.size();
        int cancelledCount = cancelledLesson.size();
        int attendedCount = attendedLessons.size();

        // Display the learner's report
        System.out.println("Learner: " + learner.getName());
        System.out.println("  Booked Lessons: " + bookedCount);
        System.out.println("  Cancelled Lessons: " + cancelledCount);
        System.out.println("  Attended Lessons: " + attendedCount);
    }

    public void fetchCoachReport(int month) {
        // Map to store the sum of ratings and the count of ratings for each coach
        Map<String, Integer> ratingSumMap = new HashMap<>();
        Map<String, Integer> ratingCountMap = new HashMap<>();

        // Iterate over all lessons for the specified month
        for (Lesson lesson : lessons) {
            if (lesson.getLessonMonth() == month && lesson.getRating() > 0) {
                // Get the coach for this lesson
                String coach = lesson.getCoach();

                // Update rating sum and count for the coach
                int rating = lesson.getRating();
                ratingSumMap.put(coach, ratingSumMap.getOrDefault(coach, 0) + rating);
                ratingCountMap.put(coach, ratingCountMap.getOrDefault(coach, 0) + 1);
            }
        }

        // Print the monthly coach report
        System.out.println("Monthly Coach Report for Month " + month + ":");
        System.out.println();
        System.out.println();
        for (Map.Entry<String, Integer> entry : ratingSumMap.entrySet()) {
            String coach = entry.getKey();
            int ratingSum = entry.getValue();
            int ratingCount = ratingCountMap.get(coach);
            double averageRating = (double) ratingSum / ratingCount;

            System.out.println("Coach: " + coach + ", Average Rating: " + averageRating);
        }
    }



}
