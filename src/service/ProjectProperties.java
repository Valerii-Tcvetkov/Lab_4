package service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class ProjectProperties {
    public static Properties loadProperties(){
        Properties properties = new Properties();
        try {
            Reader reader = new FileReader("src/property.property");
            try {
                properties.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }
}
