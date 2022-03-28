package com.company.model;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TempRowReader {
    Row currentRow;
    CSVReader reader;
    String path;

    public TempRowReader(String path) throws IOException, CsvValidationException {
        reader = new CSVReader(new FileReader(path));
        this.path = path;
        readNextLine();
    }

    private void readNextLine() throws IOException, CsvValidationException {
        String[] lineInArray;
        if ((lineInArray = reader.readNext()) != null) {
            currentRow = new Row(lineInArray);
        } else {
            currentRow = null;
            reader.close();
            // the file is a temp file so it should be deleted on finish
            deleteFile();
        }
    }

    void deleteFile() {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public void writeAndReadNextLine(CSVWriter writer) throws IOException, CsvValidationException {
        if (currentRow != null) {
            currentRow.writeRowToFile(writer);
        }
        readNextLine();
    }

    public Row getCurrentRow() {
        return currentRow;
    }
}
