package com.company;

import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Default values in case the initial args are not good
        int keyIndex = 0;
        long maxNumberOfEntries = 10;
        String path = "DefaultFile.csv";
        String outputPath = "Output.csv";

        // validating input of "key index" , "file path" , "max entries in memory"
        if (args.length >= 3) {
            if (args[1].substring(args[1].length() - 4).equals(".csv")) {
                path = args[1];
            } else {
                System.err.println("You must enter a valid csv file");
                return;
            }
            // validating that the index is an int and that the number of entries is an int
            if (intError(args[0])) {
                System.err.println("The key index must be an integer");
                return;
            }
            if (longError(args[2])) {
                System.err.println("The num of entries must be a long");
                return;
            }
            keyIndex = Integer.parseInt(args[0]);
            maxNumberOfEntries = Long.parseLong(args[2]);
            if (args.length == 4) {
                if (args[3].substring(args[3].length() - 4).equals(".csv")) {
                    outputPath = args[3];
                } else {
                    System.err.println("You must enter a valid csv file as the output path");
                    return;
                }
            }
        }

        if (args.length > 0 && args.length < 3) {
            System.err.println("You must enter 0, 3 or 4 args.");
            return;
        }

        if (maxNumberOfEntries < 2) {
            System.err.println("We can not compare one value");
            return;
        }

        // creating a directory to store the temp files and to ensure the proper deleting of files after crash
        String tempDirectory = createTempDirectory();


        // parsing the data according to chunks into temp file
        MapToFiles mapToFiles = new MapToFiles(keyIndex, path, maxNumberOfEntries, tempDirectory);
        try {
            mapToFiles.initiate();
        } catch (IOException e) {
            System.err.println("Error dealing with the original file given");
            deleteTempDirectory(tempDirectory);
            return;
        } catch (CsvValidationException e) {
            System.err.println("Error with the csv file given");
            deleteTempDirectory(tempDirectory);
            return;
        }

        try {
            MergeFiles mergeFiles = new MergeFiles(mapToFiles.tempFileCounter, keyIndex, maxNumberOfEntries, tempDirectory, outputPath);
            mergeFiles.mergeFiles();
        } catch (IOException e) {
            System.err.println("Error dealing with the second stage files");
        } catch (CsvValidationException e) {
            System.err.println("Error with the temp csv files");
        } finally {
            deleteTempDirectory(tempDirectory);
        }
    }

    private static String createTempDirectory() {
        File dir = new File("temporary");
        dir.mkdir();
        return dir.getAbsolutePath();
    }

    public static boolean intError(String input) {
        try {
            Integer.parseInt(input);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static boolean longError(String input) {
        try {
            Long.parseLong(input);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }


    /**
     * Deleting the directory after running
     *
     * @param path the path of the directory
     */
    private static void deleteTempDirectory(String path) {
        try {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                File[] entries = file.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        deleteTempDirectory(entry.getAbsolutePath());
                    }
                }
            }
            if (!file.delete()) {
                throw new IOException("Failed to delete " + file);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
