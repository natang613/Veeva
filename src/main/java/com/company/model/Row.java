package com.company.model;

import com.opencsv.CSVWriter;

public class Row {
    private String[] words;

    public Row(String[] words) {
        this.words = words;
    }

    public void writeRowToFile(CSVWriter writer) {
        writer.writeNext(words, false);
    }

    public String[] getWords(){
        return words;
    }
}
