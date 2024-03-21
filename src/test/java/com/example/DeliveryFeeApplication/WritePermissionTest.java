package com.example.DeliveryFeeApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WritePermissionTest {

    public static void main(String[] args) {
        String directoryPath = "C:/Users/Kasutaja/Desktop/MyDatabase";
        testWritePermission(directoryPath);
    }

    public static void testWritePermission(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            System.out.println("Directory does not exist: " + directoryPath);
            return;
        }

        if (!directory.isDirectory()) {
            System.out.println("Path is not a directory: " + directoryPath);
            return;
        }

        File testFile = new File(directory, "test.txt");

        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write("Testing write permissions");
            writer.close();
            System.out.println("Write test successful. File written to: " + testFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to write file. Check write permissions for directory: " + directoryPath);
            e.printStackTrace();
        } if (testFile.exists()) {
            if (testFile.delete()) {
                System.out.println("Test file deleted successfully: " + testFile.getAbsolutePath());
            } else {
                System.out.println("Failed to delete test file: " + testFile.getAbsolutePath());
            }
        }
    }
}