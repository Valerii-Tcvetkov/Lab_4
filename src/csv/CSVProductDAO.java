package csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import daos.ProductDAO;
import objects.Product;

import java.io.IOException;

public class CSVProductDAO implements ProductDAO {
    CSVConstructor constructor = new CSVConstructor("src/csvFiles/Product.csv");

    @Override
    public Integer getID(String name) {
        try {
            String[] strings;
            CSVReader reader = this.constructor.getReader();
            while ((strings = reader.readNext()) != null) {
                if (strings[1].equals(name)) {
                    reader.close();
                    return Integer.parseInt(strings[0]);
                }
            }
            reader.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getName(Integer ID) {
        try {
            String[] strings;
            CSVReader reader = this.constructor.getReader();
            while ((strings = reader.readNext()) != null) {
                if (ID.equals(Integer.parseInt(strings[0]))) {
                    reader.close();
                    return strings[1];
                }
            }
            reader.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer getLastID() {
        try {
            String[] strings;
            Integer lastID = -1;
            CSVReader reader = this.constructor.getReader();
            while ((strings = reader.readNext()) != null) {
                if (Integer.parseInt(strings[0]) > lastID) lastID = Integer.parseInt(strings[0]);
            }
            reader.close();
            if (lastID.equals(-1)) return 0;
            return lastID;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product getProductByName(String name) {
        try {
            CSVReader reader = this.constructor.getReader();
            String [] strings;
            while ((strings = reader.readNext()) != null){
                if (strings[1].equals(name)){
                    reader.close();
                    return new Product(Integer.parseInt(strings[0]), strings[1]);
                }
            }
            reader.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product getProductByID(Integer id) {
        try {
            CSVReader reader = this.constructor.getReader();
            String [] strings;
            while ((strings = reader.readNext()) != null){
                Integer readID = Integer.parseInt(strings[0]);
                if (readID.equals(id)){
                    reader.close();
                    return new Product(Integer.parseInt(strings[0]), strings[1]);
                }
            }
            reader.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(Product object) {
        try {
            CSVWriter writer = this.constructor.appEnd();
            String [] record = {String.valueOf(object.id), object.name};
            writer.writeNext(record);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product update(Product object) {
        return null;
    }
}
