package csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVConstructor {
    private String path;

    CSVConstructor(String path) {
        this.path = path;
    }

    public CSVReader getReader() {
        try {
            return new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CSVWriter appEnd(){
        try {
            return new CSVWriter(new FileWriter(path, true), ',');
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CSVWriter getWriter(){
        try {
            return new CSVWriter(new FileWriter(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
