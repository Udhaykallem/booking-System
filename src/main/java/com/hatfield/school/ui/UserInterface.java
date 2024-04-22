package com.hatfield.school.ui;

import com.hatfield.school.entities.Coach;
import com.hatfield.school.entities.Learner;
import com.hatfield.school.entities.Lesson;
import com.hatfield.school.services.Timetable;

import java.util.*;

public class UserInterface {
    private Timetable timetable;
    private Scanner scanner;

    public UserInterface(Timetable timetable) {
        this.timetable = timetable;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("Welcome to Hatfield Junior Swimming School!");
            System.out.println("1. Book a swimming lesson");
            System.out.println("2. Change/Cancel a booking");
            System.out.println("3. Attend a swimming lesson");
            System.out.println("4. Monthly learner report");
            System.out.println("5. Monthly coach report");
            System.out.println("6. Register a new learner");
            System.out.println("0. Exit");
            System.out.print("Choose an option:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    BookSwimmingLesson();
                    break;
                case 2:
                    ChangeOrCancelBooking();
                    break;
                case 3:
                    attendLesson();
                    break;
                case 4:
                    generateMonthlyLearnerReport();
                    break;
                case 5:
                    generateMonthlyCoachReport();
                    break;
                case 6:
                    addNewLearner();
                    break;
                case 7:
                    listAllLearners();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return; // Exit the method and program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public void listAllLearners() {
        List<Learner> learners = Learner.getAllLearners();

        if (learners.isEmpty()) {
            System.out.println("No learners found.");
        } else {
            System.out.println("List of all learners:");
            for (Learner learner : learners) {
                System.out.println(learner.getName() + " Grade level- " + learner.getGradeLevel());
            }
        }
    }


    public void addNewLearner() {
        System.out.println("Enter learner details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Gender (M/F): ");
        char gender = scanner.nextLine().charAt(0);

        int age;
        do {
            System.out.print("Age (4-11): ");
            age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (age < 4 || age > 11) {
                System.out.println("Age must be between 4 and 11.");
            }
        } while (age < 4 || age > 11);

        System.out.print("Emergency Contact: ");
        String emergencyContact = scanner.nextLine();

        int gradeLevel;
        do {
            System.out.print("Grade Level (0-5) (0, if new to swimming): ");
            gradeLevel = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (gradeLevel < 0 || gradeLevel > 5) {
                System.out.println("Grade level must be between 0 and 5.");
            }
        } while (gradeLevel < 0 || gradeLevel > 5);

        Learner.addNewLearner(name, gender, age, emergencyContact, gradeLevel);
        System.out.println("New learner added successfully.");
    }

    public void BookSwimmingLesson() {
        System.out.println("How would you like to view the timetable? ");
        System.out.println("1. View Timetable by Day");
        System.out.println("2. View Timetable by Grade Level");
        System.out.println("3. View Timetable by Coach Name");
        System.out.print("Enter your choice: ");

        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > 3) {
                    throw new IllegalArgumentException("Invalid choice. Please enter a number between 1 and 3.");
                }
                break; // Exit the loop if the input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        switch (choice) {
            case 1:
                viewTimetableByDay();
                break;
            case 2:
                viewTimetableByGradeLevel();
                break;
            case 3:
                viewTimetableByCoach();
                break;
        }
    }


    private void ChangeOrCancelBooking() {
        System.out.println("Choose an option to proceed ");
        System.out.println("1. Modify your Booking");
        System.out.println("2. Delete Your Booking");
        System.out.print("Enter your choice: ");

        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > 2) {
                    throw new IllegalArgumentException("Invalid choice. Please enter a number between 1 and 3.");
                }
                break; // Exit the loop if the input is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Display booking options after viewing the timetable
        System.out.println("Booking Options:");
        switch (choice) {
            case 1:
                updateBooking();
                break;
            case 2:
                cancelLesson();
                break;
        }
    }

    private void viewTimetableByDay() {
        // Prompt for the day
        System.out.print("Enter the day (Monday, Wednesday, Friday, Saturday): ");
        String day = scanner.nextLine();

        // Get lessons for the selected day
        List<Lesson> lessons = timetable.getLessonsByDay(day);

        if (lessons.isEmpty()) {
            System.out.println("No lessons available for " + day);
            return;
        }

        // Display lessons for the selected day
        System.out.println("Lessons for " + day + ":");
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            System.out.println((i + 1) + ". Lesson at " + lesson.getTimeSlot() + " with " + lesson.getCoach() + " for Grade " + lesson.getGrade() + " for Week " + lesson.getWeekNumber() + " [Vacancy(" + lesson.getCapacity() + ")]");
        }

        // Prompt the user to select a lesson and book it
        System.out.print("Enter the index of the lesson you want to book (enter 0 to return to the main menu): ");
        int lessonIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (lessonIndex == 0) {
            return; // Return to the main menu
        }

        // Get the lesson ID corresponding to the selected index
        String lessonId = lessons.get(lessonIndex - 1).getId();
        bookLesson(lessonId);
    }

    private void viewTimetableByCoach() {
        while (true) {
            System.out.println("Enter the coach's name (Jack, Kevin, Jason, Jessica): ");
            System.out.println("1. Jack");
            System.out.println("2. Kevin");
            System.out.println("3. Jason");
            System.out.println("4. Hellen");
            System.out.println("0. Return to main menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 0:
                    return; // Return to the main menu
                case 1:
                    if (viewTimetableByCoach("Jack")) return;
                    break;
                case 2:
                    if (viewTimetableByCoach("Kevin")) return;
                    break;
                case 3:
                    if (viewTimetableByCoach("Jason")) return;
                    break;
                case 4:
                    if (viewTimetableByCoach("Hellen")) return;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 0 and 4.");
                    break;
            }
        }
    }

    private boolean viewTimetableByCoach(String coachName) {
        List<Lesson> lessons = timetable.getLessonsByCoach(coachName);

        if (lessons.isEmpty()) {
            System.out.println("No lessons available for coach " + coachName);
            return false;
        }

        System.out.println("Lessons for coach " + coachName + ":");
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            System.out.println((i + 1) + ". " + lesson.getDay() + " at " + lesson.getTimeSlot() + " for Grade " + lesson.getGrade());
        }

        // Prompt the user to select a lesson and book it
        System.out.print("Enter the index of the lesson you want to book (enter 0 to return to the main menu): ");
        int lessonIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (lessonIndex == 0) {
            return false; // Return to the main menu
        }

        // Get the lesson ID corresponding to the selected index
        String lessonId = lessons.get(lessonIndex - 1).getId();
        bookLesson(lessonId);

        // If booking is successful, return true to indicate to the caller that it should return to the main menu
        System.out.println("Booking successful. Returning to the main menu...");
        return true;
    }


    private void viewTimetableByGradeLevel() {
        try {
            System.out.print("Enter the grade level (0-5): ");
            int gradeLevel = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Get lessons by grade level
            List<Lesson> lessons = timetable.getLessonsByGradeLevel(gradeLevel);

            // Display lessons with unique IDs
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                System.out.println((i + 1) + ". Lesson at " + lesson.getTimeSlot() + " by " + lesson.getCoach() + " for week " + lesson.getWeekNumber() + "(Vacancy - " + lesson.getCapacity() + ")");
            }

            // Prompt the user to select a lesson and book it
            System.out.print("Enter the index of the lesson you want to book(enter 0 to return to the main menu): ");
            int lessonIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (lessonIndex == 0) {
                return; // Return to the main menu
            }

            // Validate the input lesson index
            if (lessonIndex < 1 || lessonIndex > lessons.size()) {
                System.out.println("Invalid input. Please enter a number between 1 and " + lessons.size() + ".");
                return;
            }

            // Get the lesson ID corresponding to the selected index
            String lessonId = lessons.get(lessonIndex - 1).getId();
            bookLesson(lessonId);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Consume invalid input
        }
    }


    private void updateBooking() {
        System.out.println("Modify your Bookings");
        System.out.print("Enter your name: ");
        String learnerName = scanner.nextLine();

        Learner learner = null;
        for (Learner l : Learner.getAllLearners()) {
            if (l.getName().equalsIgnoreCase(learnerName)) {
                learner = l;
                break;
            }
        }

        if (learner == null) {
            System.out.println("Learner not found. Please try again.");
            return;
        }

        // Display the lessons booked by the learner
        System.out.println("Lessons booked by " + learner.getName() + ":");
        List<Lesson> learnerLessons = timetable.getLearnerLessons(learner);
        if (learnerLessons.isEmpty()) {
            System.out.println("No lessons booked by " + learner.getName());
            return;
        }
        for (int i = 0; i < learnerLessons.size(); i++) {
            Lesson lesson = learnerLessons.get(i);
            System.out.println((i + 1) + ". " + lesson); // Display lesson details with index
        }

        // Prompt the user to select a lesson to update
        System.out.print("Enter the index of the lesson you want to update: ");
        int lessonIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (lessonIndex < 1 || lessonIndex > learnerLessons.size()) {
            System.out.println("Invalid lesson index. Please try again.");
            return;
        }

        // Get the lesson to update
        Lesson lessonToUpdate = learnerLessons.get(lessonIndex - 1);

        // Cancel the existing lesson
        timetable.cancelLesson(learner, lessonToUpdate);

        // Prompt the user to choose how to book a new lesson
        System.out.println("Update your Lesson - ");
        System.out.println("1. View Timetable by Day");
        System.out.println("2. View Timetable by Grade Level");
        System.out.println("3. View Timetable by Coach's Name");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewTimetableByDay();
                break;
            case 2:
                viewTimetableByGradeLevel();
                break;
            case 3:
                viewTimetableByCoach();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("Lesson Updated Successfully for " + learnerName);
    }


    private void bookLesson(String lessonId) {
        System.out.println(lessonId);
        // Prompt for learner's name
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Find the learner by name
        List<Learner> learners = Learner.getAllLearners();
        Learner learner = null;
        for (Learner l : learners) {
            if (l.getName().equalsIgnoreCase(name)) {
                learner = l;
                break;
            }
        }

        // Check if learner is found
        if (learner == null) {
            System.out.println("Learner not found.");
            return;
        }

        // Find the lesson by ID
        Lesson lesson = timetable.getLessonById(lessonId);
        if (lesson == null) {
            System.out.println("Lesson not found.");
            return;
        }

        // Book the lesson for the learner
        boolean bookingSuccessful = timetable.bookLesson(learner, lesson);
        if (bookingSuccessful) {
            System.out.println("Lesson " + lesson.getDay() + " at " + lesson.getTimeSlot() + " booked successfully for " + learner.getName() + ".");
        } else {
            System.out.println("Failed to book lesson. Please try again later.");
        }
    }

    private Lesson findLessonById(String lessonId) {
        for (Lesson lesson : timetable.getLessons()) {
            if (lesson.getId().equals(lessonId)) {
                return lesson;
            }
        }
        return null;
    }

    private void cancelLesson() {
        // Prompt the user to enter their name
        System.out.print("Enter your name: ");
        String learnerName = scanner.nextLine();

        // Find the learner by name
        Learner learner = null;
        for (Learner l : Learner.getAllLearners()) {
            if (l.getName().equalsIgnoreCase(learnerName)) {
                learner = l;
                break;
            }
        }

        if (learner == null) {
            System.out.println("Learner not found. Please try again.");
            return;
        }

        // Display the lessons booked by the learner
        System.out.println("Lessons booked by " + learner.getName() + ":");
        Set<Lesson> uniqueLessons = new HashSet<>(timetable.getLearnerLessons(learner));
        if (uniqueLessons.isEmpty()) {
            System.out.println("No lessons booked by " + learner.getName());
            return;
        }
        int i = 1;
        for (Lesson lesson : uniqueLessons) {
            System.out.println((i++) + ". " + lesson); // Display lesson details with index
        }

        // Prompt the user to select a lesson to cancel
        System.out.print("Enter the index of the lesson you want to cancel: ");
        int lessonIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (lessonIndex < 1 || lessonIndex > uniqueLessons.size()) {
            System.out.println("Invalid lesson index. Please try again.");
            return;
        }

        // Get the lesson to cancel
        Lesson lessonToCancel = new ArrayList<>(uniqueLessons).get(lessonIndex - 1);

        // Cancel the lesson
        timetable.cancelLesson(learner, lessonToCancel);

        displayMenu();
    }

    private void viewAllLessons() {
        List<Lesson> lessons = timetable.getLessons();
        if (lessons.isEmpty()) {
            System.out.println("No lessons available.");
        } else {
            System.out.println("All Lessons:");
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                System.out.println((i + 1) + ". " + lesson); // Display lesson details with index
            }
        }
    }

    // Method to find predefined learner by name
    private Learner findPredefinedLearner(String learnerName) {
        for (Learner learner : timetable.getLearners()) {
            if (learner.getName().equalsIgnoreCase(learnerName)) {
                return learner;
            }
        }
        return null; // Return null if learner is not found
    }

    private void viewAllCoaches() {
        List<Coach> coaches = Coach.getAllCoaches();
        System.out.println("All Coaches:");
        if (coaches.isEmpty()) {
            System.out.println("No coaches found.");
        } else {
            for (int i = 0; i < coaches.size(); i++) {
                Coach coach = coaches.get(i);
                System.out.println((i + 1) + ". " + coach.getName());
            }
        }
    }

    private void viewAllLearners() {
        List<Learner> learners = Learner.getAllLearners();
        System.out.println("All Learners:");
        for (int i = 0; i < learners.size(); i++) {
            Learner learner = learners.get(i);
            System.out.println((i + 1) + ". " + learner.getName() + "  Grade level -" + learner.getGradeLevel());
        }
    }

    private void attendLesson() {
        // Prompt the user to enter their name
        System.out.print("Enter your name: ");
        String learnerName = scanner.nextLine();

        // Find the learner by name
        Learner learner = null;
        for (Learner l : Learner.getAllLearners()) {
            if (l.getName().equalsIgnoreCase(learnerName)) {
                learner = l;
                break;
            }
        }

        if (learner == null) {
            System.out.println("Learner not found.");
            return;
        }


        // Display lessons booked by the learner
        List<Lesson> learnerLessons = timetable.getLearnerLessons(learner);
        if (learnerLessons.isEmpty()) {
            System.out.println("No lessons booked by " + learner.getName());
            return;
        }

        System.out.println("Lessons booked by " + learner.getName() + ":");
        for (int i = 0; i < learnerLessons.size(); i++) {
            Lesson lesson = learnerLessons.get(i);
            System.out.println((i + 1) + ". " + lesson); // Display lesson details with index
        }

        // Prompt the user to select a lesson to attend
        System.out.print("Enter the index of the lesson you want to attend: ");
        int lessonIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Validate the lesson index
        if (lessonIndex < 1 || lessonIndex > learnerLessons.size()) {
            System.out.println("Invalid lesson index. Please try again.");
            return;
        }
        System.out.println(learnerLessons);
        // Get the lesson corresponding to the selected index
        Lesson selectedLesson = learnerLessons.get(lessonIndex - 1);

        // Mark the learner as attended for the selected lesson
        if(timetable.attendLesson(learner, selectedLesson)){
            System.out.print("Enter the rating for this lesson: ");
            int rating = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character after nextInt()

            timetable.submitRating(selectedLesson, rating);

            System.out.print("Enter the review for this lesson: ");
            String review = scanner.nextLine();
            timetable.addReview(selectedLesson, learner, review);
        }
        displayMenu();
    }

    public void generateMonthlyLearnerReport() {
        System.out.print("Enter the month number (e.g., 03 for March): ");
        int month = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter the learner's name or 'all': ");
        String learnerName = scanner.nextLine();

        if (learnerName.equalsIgnoreCase("all")) {
            // Fetch reports for all learners
            for (Learner learner : timetable.getLearners()) {
                timetable.fetchLearnerReport(learner, month);
            }
        } else {
            Learner learner = null;
            for (Learner l : Learner.getAllLearners()) {
                if (l.getName().equalsIgnoreCase(learnerName)) {
                    learner = l;
                    break;
                }
            }

            if (learner == null) {
                System.out.println("Learner not found.");
                return;
            }

            // Fetch the learner's report for the specified month
            timetable.fetchLearnerReport(learner, month);
        }
    }

    public void generateMonthlyCoachReport() {
        System.out.print("Enter the month number (e.g., 03 for March): ");
        int month = scanner.nextInt();
        timetable.fetchCoachReport(month);
        }

}


