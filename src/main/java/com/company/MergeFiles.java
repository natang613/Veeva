package com.company;

import com.company.model.TempRowReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MergeFiles {
    String tempDirectory;
    private final long maxEntriesInMemory;
    final int keyIndex;
    List<TempRowReader> currentReaders = new ArrayList<>();

    // this keeps track of the temp file series that is to be parsed either _tempXXX.csv or tempXXX.csv
    boolean evenIteration = true;

    // this keeps track of the number of new temp files created after the merge
    long numberOfNewTempFiles = 0;

    // this keeps track of the number of temp files being merged in this iteration of the temp files
    long numTempFilesInCurrentIteration;

    String outputPath;

    MergeFiles(long numTempFilesToStart, int keyIndex, long maxEntriesInMemory, String tempDirectory, String outputPath) {
        this.keyIndex = keyIndex;
        this.numTempFilesInCurrentIteration = numTempFilesToStart;
        this.maxEntriesInMemory = maxEntriesInMemory;
        this.tempDirectory = tempDirectory;
        this.outputPath = outputPath;
    }

    /**
     * The controller function that runs the merge on the files, this function is called in recursion until the
     * all files are merged into one file
     *
     * @throws IOException            if the file is in any way unreadable an IO exception is thrown
     * @throws CsvValidationException if the file is not csv file or corrupted this is thrown
     */
    void mergeFiles() throws IOException, CsvValidationException {
        // final iteration if all files can fit into memory of maxEntries
        boolean finalIteration = numTempFilesInCurrentIteration <= maxEntriesInMemory;
        for (long i = 0; i < numTempFilesInCurrentIteration; i++) {
            currentReaders.add(new TempRowReader(tempDirectory + File.separator + (evenIteration ? "" : "_") + "temp" + i + ".csv"));
            if ((i + 1) % maxEntriesInMemory == 0) {
                merge(finalIteration);
            }
        }

        // checking that there are no overflow files that weren't run yet
        if (currentReaders.size() > 0) {
            merge(finalIteration);
        }
        // This part checks that there are still temporary files to merge together. Whether they are from the first
        // merging iteration or any subsequent iteration
        if (!finalIteration) {
            resetAndMergeAgain();
        }
    }

    private void resetAndMergeAgain() throws IOException, CsvValidationException {
        evenIteration = !evenIteration;
        numTempFilesInCurrentIteration = numberOfNewTempFiles;
        numberOfNewTempFiles = 0;
        mergeFiles();
    }

    /***
     * The main function that merges the files that can fit into the allocated memory into one.
     * Merging the files line by line. Looping over all of the lines currently in the object and appending the smallest one
     * to the new csv file
     * @param finalIteration to know if we are appending to a temp file or output file
     * @throws IOException throws the exception when there is an io problem
     * @throws CsvValidationException  throws the exception when there is csv problem
     */
    void merge(boolean finalIteration) throws IOException, CsvValidationException {

        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = createOutputWriter(finalIteration);

        while (currentReaders.size() > 0) {
            int minimumReaderIndex = -1;

            for (int i = 0; i < currentReaders.size(); i++) {
                if (minimumReaderIndex == -1 || numberIsSmallerThanCurrentMinimum(i, minimumReaderIndex)) {
                    minimumReaderIndex = i;
                }
            }
            currentReaders.get(minimumReaderIndex).writeAndReadNextLine(writer);
            // checking if the file reached the end
            if (currentReaders.get(minimumReaderIndex).getCurrentRow() == null) {
                currentReaders.remove(minimumReaderIndex);
            }
        }
        writer.close();
        numberOfNewTempFiles++;
    }

    private boolean numberIsSmallerThanCurrentMinimum(int i, int minimumReaderIndex) {
        return currentReaders.get(i).getCurrentRow().getWords()[keyIndex].replaceAll("[\uFEFF-\uFFFF]", "").compareTo(currentReaders.get(minimumReaderIndex).getCurrentRow().getWords()[keyIndex].replaceAll("[\uFEFF-\uFFFF]", "")) < 0;
    }

    private CSVWriter createOutputWriter(boolean finalIteration) throws IOException {
        FileWriter outputfile;
        if (finalIteration) {
            outputfile = new FileWriter(outputPath);
        } else {
            outputfile = new FileWriter(tempDirectory + File.separator + (evenIteration ? "_" : "") + "temp" + numberOfNewTempFiles + ".csv");
        }
        return new CSVWriter(outputfile);
    }

}
