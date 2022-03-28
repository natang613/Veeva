package com.company;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

class MainTest {

    @Test
    void main() throws IOException, CsvValidationException {
        String outputPath = "testOutput.csv";
        int key = 1;
        Main.main(new String[]{String.valueOf(key), "DefaultFile.csv", "10", outputPath});
        CSVReader reader = new CSVReader(new FileReader(outputPath));
        String previousValue = "";
        String[] lineInArray;
        while ((lineInArray = reader.readNext()) != null) {
            if (!previousValue.equals("")) {
                Assert.assertTrue(lineInArray[key].compareTo(previousValue) > 0);
            }
            previousValue = lineInArray[key];
        }

    }
}