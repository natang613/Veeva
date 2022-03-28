package com.company;

import com.company.model.Row;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapToFiles {
    final long maxNumberOfEntries;
    final String path;
    final int keyIndex;
    long tempFileCounter = 0;  // to keep track what temp file number we are up to
    String tempDirectory;

    public MapToFiles(int key, String path, long maxNumberOfEntries, String tempDirectory) {
        this.maxNumberOfEntries = maxNumberOfEntries;
        this.keyIndex = key;
        this.path = path;
        this.tempDirectory = tempDirectory;

    }

    /**
     * The controlling function that insures that we do not read more entries into memory than allowed
     *
     * @throws IOException            if the is problem with reading the file or writing to the new file
     * @throws CsvValidationException if the csv file is corrupted in any way
     */
    public void initiate() throws IOException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader(path));
        String[] lineInArray;
        List<Row> rowList = new ArrayList<>();
        long rowCounter = 0;
        while ((lineInArray = reader.readNext()) != null) {
            rowCounter++;
            rowList.add(new Row(lineInArray));
            if (rowCounter % maxNumberOfEntries == 0) {
                sortAndWrite(rowList);
                tempFileCounter++;
                rowList.clear();
            }
        }
        // handling the entries that did not get sorted yet
        if (rowList.size() > 0) {
            sortAndWrite(rowList);
            tempFileCounter++;
            rowList.clear();
        }
        reader.close();
    }

    void sortAndWrite(List<Row> rowList) throws IOException {

        quickSort(rowList, 0, rowList.size() - 1);

        FileWriter fileWriter;
        fileWriter = new FileWriter(tempDirectory + File.separator + "temp" + tempFileCounter + ".csv");
        // create CSVWriter object filewriter object as parameter

        CSVWriter writer = new CSVWriter(fileWriter);
        for (Row row : rowList) {
            row.writeRowToFile(writer);
        }
        writer.close();
    }

    /**
     * Sorting the list in O(1) space
     *
     * @param rowList the list to sort
     * @param low     the low index of the list
     * @param high    the upper index of list
     */
    public void quickSort(List<Row> rowList, int low, int high) {
        if (low < high) {
            int pi = partition(rowList, low, high);

            // sort elements before  partition and after partition
            quickSort(rowList, low, pi - 1);
            quickSort(rowList, pi + 1, high);
        }
    }

    private int partition(List<Row> array, int low, int high) {
        // using a random pivot in order to make the average runtime faster
        int randIndex = getRandomNumberUsingInts(low, high + 1);
        quickSortSwap(array, randIndex, high);
        Row pivot = array.get(high);

        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            // cleaning empty characters from the strings
            if (smallerThanPivot(array, j, pivot)) {
                i++;
                quickSortSwap(array, i, j);
            }
        }
        quickSortSwap(array, i + 1, high);
        return (i + 1);
    }

    private boolean smallerThanPivot(List<Row> array, int j, Row pivot) {
        // removing the empty characters from the strings
        String currentWord = array.get(j).getWords()[keyIndex].replaceAll("[\uFEFF-\uFFFF]", "");
        String pivotWord = pivot.getWords()[keyIndex].replaceAll("[\uFEFF-\uFFFF]", "");
        return currentWord.compareTo(pivotWord) < 0;
    }

    public static int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public static void quickSortSwap(List<Row> array, int i, int j) {
        Row temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }
}
