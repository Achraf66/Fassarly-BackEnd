package com.fassarly.academy.utils;

public class UniqueFileNameGenerator {

    public static String generateUniqueFileName(String originalFileName) {

        long timestamp = System.currentTimeMillis();
        int randomNumber = (int)(Math.random() * 10000);
        String extension = getFileExtension(originalFileName);
        String uniqueFileName = timestamp + "_" + randomNumber + extension;

        return uniqueFileName;
    }

    private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            return fileName.substring(index);
        } else {
            return "";
        }
    }
}