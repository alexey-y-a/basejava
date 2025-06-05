package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {

    public static void main(String[] args) {
        File projectDir = new File(".");
        System.out.println("Рекурсивный обход файлов и папок в проекте:");
        printDirectoryDeeply(projectDir, "");
    }

    private static void printDirectoryDeeply(File dir, String indent) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(indent + "File: " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println(indent + "Directory: " + file.getName());
                    printDirectoryDeeply(file, indent + "  ");
                }
            }
        }
    }
}
