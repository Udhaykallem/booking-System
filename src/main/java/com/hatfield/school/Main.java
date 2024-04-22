package com.hatfield.school;

import com.hatfield.school.services.Timetable;
import com.hatfield.school.ui.UserInterface;

import java.io.*;

public class Main implements Serializable {
    public static void main(String[] args) {
        Timetable timetable = loadTimetable();
        if (timetable == null) {
            timetable = new Timetable(java.time.MonthDay.now().getMonthValue());
            timetable.generateTimetable(java.time.MonthDay.now().getMonthValue());
        }

        final Timetable finalTimetable = timetable;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> saveTimetable(finalTimetable)));

        UserInterface userInterface = new UserInterface(timetable);
        userInterface.displayMenu();
    }

    private static Timetable loadTimetable() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("timetable.ser"))) {
            return (Timetable) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Timetable file not found. Generating new timetable.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void saveTimetable(Timetable timetable) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("timetable.ser"))) {
            oos.writeObject(timetable);
            System.out.println("Timetable saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
