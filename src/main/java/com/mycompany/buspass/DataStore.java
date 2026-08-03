package com.mycompany.buspass;

import java.io.*;
import java.util.*;

public class DataStore {
    private static final String FILE_NAME = "students.dat";
    private static Map<String, Student> students = new LinkedHashMap<>();
    private static Student currentStudent = null;

    static {
        load();
    }

    @SuppressWarnings("unchecked")
    private static void load() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            students = (Map<String, Student>) ois.readObject();
        } catch (Exception e) {
            students = new LinkedHashMap<>();
        }
    }

    public static void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean idExists(String id) {
        return students.containsKey(id);
    }

    public static void addStudent(Student s) {
        students.put(s.getStudentId(), s);
        save();
    }

    public static Student authenticate(String id, String password) {
        Student s = students.get(id);
        if (s != null && s.getPassword().equals(password)) return s;
        return null;
    }

    public static Student getCurrentStudent() { return currentStudent; }
    public static void setCurrentStudent(Student s) { currentStudent = s; }

    public static void updateCurrentStudent() {
        if (currentStudent != null) {
            students.put(currentStudent.getStudentId(), currentStudent);
            save();
        }
    }

    public static String nextApplicationId() {
        return "APP-2026-" + (10000 + students.size() * 3 + new Random().nextInt(900));
    }

    public static String nextPassNumber() {
        return "BP-2026-" + (1000 + new Random().nextInt(8999));
    }

   
    public static boolean resetPassword(String studentId, String newPassword) {
        Student s = students.get(studentId);
        if (s != null) {
            s.setPassword(newPassword); 
            save(); 
            return true;
        }
        return false;
    }
}