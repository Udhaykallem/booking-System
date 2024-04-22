package com.hatfield.school.entities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Learner implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private char gender;
    private int age;
    private String emergencyContact;
    private int gradeLevel;
    private List<Lesson> bookedLessons;

    private static List<Learner> learners = new ArrayList<>();
    private static final String LEARNERS_FILE_PATH = "learners.ser";

    public Learner(String name, char gender, int age, String emergencyContact, int gradeLevel) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.emergencyContact = emergencyContact;
        this.gradeLevel = gradeLevel;
        this.bookedLessons = new ArrayList<>();
    }

    public static List<Learner> getAllLearners() {
        return learners;
    }

    public static void addNewLearner(String name, char gender, int age, String emergencyContact, int gradeLevel) {
        Learner newLearner = new Learner(name, gender, age, emergencyContact, gradeLevel);
        learners.add(newLearner);
        saveLearnersToFile();
    }

    private static void saveLearnersToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(LEARNERS_FILE_PATH))) {
            outputStream.writeObject(learners);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadLearnersFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(LEARNERS_FILE_PATH))) {
            learners = (List<Learner>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Learners file not found. Creating a new one.");
            saveLearnersToFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static {
        loadLearnersFromFile();
    }

    public void bookLesson(Lesson lesson) {
        if (lesson != null && hasCapacity(lesson) && isEligibleForBooking(lesson)) {
            bookedLessons.add(lesson);
            lesson.addLearner(this);
            if (gradeLevel < 5) {
                gradeLevel++;
            }
            System.out.println(name + " has successfully booked a lesson.");
        } else {
            System.out.println("Booking failed. Please check availability and eligibility.");
        }
    }

    public void cancelLesson(Lesson lesson) {
        if (bookedLessons.contains(lesson)) {
            bookedLessons.remove(lesson);
            lesson.removeLearner(this);
            System.out.println(name + " has successfully cancelled the lesson.");
        } else {
            System.out.println("Cancellation failed. Lesson not found in booked lessons.");
        }
    }

    private boolean hasCapacity(Lesson lesson) {
        return lesson.hasVacancy();
    }

    private boolean isEligibleForBooking(Lesson lesson) {
        return gradeLevel >= lesson.getGradeLevel();
    }

    public List<Lesson> getBookedLessons() {
        return bookedLessons;
    }

    public String getName() {
        return name;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public char getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public void setAttended(boolean b) {
    }

    @Override
    public String toString() {
        return "Learner{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", gradeLevel=" + gradeLevel +
                '}';
    }

    // Static block to retrieve predefined learners
    static {
        learners.add(new Learner("Alice", 'F', 10, "123456789", 5));
        learners.add(new Learner("Bob", 'M', 9, "987654321", 4));
        learners.add(new Learner("Charlie", 'M', 11, "456123789", 5));
        learners.add(new Learner("Diana", 'F', 8, "789654123", 3));
        learners.add(new Learner("Emma", 'F', 12, "321654987", 5));
        learners.add(new Learner("Frank", 'M', 7, "654321789", 2));
        learners.add(new Learner("Grace", 'F', 13, "987321654", 5));
        learners.add(new Learner("Harry", 'M', 6, "789123654", 1));
        learners.add(new Learner("Ivy", 'F', 14, "654987321", 5));
        learners.add(new Learner("Jack", 'M', 5, "321789654", 1));
        learners.add(new Learner("Katie", 'F', 15, "456789123", 5));
        learners.add(new Learner("Liam", 'M', 4, "789456123", 1));
        learners.add(new Learner("Mia", 'F', 16, "123789456", 5));
        learners.add(new Learner("Noah", 'M', 11, "456789123", 5));
        learners.add(new Learner("Olivia", 'F', 10, "789123456", 5));
    }
}
