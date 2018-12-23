package csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.sun.tools.javac.util.Pair;
import daos.ProductDAO;
import daos.ShopDAO;
import objects.Product;
import objects.Property;
import objects.Shop;

import static objects.ObjectStat.*;

public class CSVShopDAO implements ShopDAO {
    protected CSVConstructor constructor = new CSVConstructor("src/csvFiles/Shop.csv");

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
    public Shop getShopByName(String name) {
        try {
            CSVReader reader = this.constructor.getReader();
            String[] strings;
            Shop shop = null;
            while ((strings = reader.readNext()) != null) {
                if (strings[1].equals(name)) {
                    reader.close();
                    shop = new Shop(Integer.parseInt(strings[0]), strings[1], strings[2]);
                    break;
                }
            }
            reader.close();
            if (shop == null) return null;
            ProductDAO productDAO = new CSVProductDAO();
            reader = ((CSVProductDAO) productDAO).constructor.getReader();
            while ((strings = reader.readNext()) != null) {
                if (strings.length > 2) {
                    Integer i = 2;
                    while (!i.equals(strings.length)) {
                        Integer shopId = Integer.parseInt(strings[i]);
                        Integer count = Integer.parseInt(strings[i + 1]);
                        Double price = Double.parseDouble(strings[i + 2]);
                        if (shopId.equals(shop.id)) {
                            shop.addProduct(productDAO.getProductByName(strings[1]), new Property(count, price, DEFAULT));
                        }
                        i += 3;
                    }
                }
            }
            reader.close();
            return shop;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Shop findShopWithLowerCostForProduct(Product product) {
        try {
            ProductDAO productDAO = new CSVProductDAO();
            CSVReader reader = ((CSVProductDAO) productDAO).constructor.getReader();
            String[] strings;
            while ((strings = reader.readNext()) != null) {
                if (strings[1].equals(product.name)) {
                    break;
                }
            }
            Integer i = 2;
            if (strings.length < 3) return null;
            Shop answer = getShopByName(getName(Integer.parseInt(strings[i])));
            Double price = Double.parseDouble(strings[i + 2]);
            i += 3;
            while (!i.equals(strings.length)) {
                if (Double.parseDouble(strings[i + 2]) < price) {
                    price = Double.parseDouble(strings[i + 2]);
                    answer = getShopByName(getName(Integer.parseInt(strings[i])));
                }
                i += 3;
            }
            return answer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Shop findCheapestShop(List<Pair<Product, Integer>> listOfProduct) {
        try {
            CSVReader reader = this.constructor.getReader();
            String[] strings;
            Shop answer = null;
            Double cost = null;
            while ((strings = reader.readNext()) != null && cost == null) {
                cost = getShopByName(strings[1]).getPriceOfProductList(listOfProduct);
                if (cost != null) {
                    answer = getShopByName(strings[1]);
                    break;
                }
            }
            while ((strings = reader.readNext()) != null) {
                Double tempCost = getShopByName(strings[1]).getPriceOfProductList(listOfProduct);
                if (tempCost != null) {
                    if (cost > tempCost) {
                        answer = getShopByName(strings[1]);
                        cost = tempCost;
                    }
                }
            }
            return answer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void checkProductList(Shop shop) {
        try {
            ProductDAO productDAO = new CSVProductDAO();
            CSVReader reader = new CSVConstructor("src/csvFiles/Product.csv").getReader();
            CSVWriter writer = new CSVConstructor("src/csvFiles/Temp.csv").getWriter();
            String[] strings;
            while ((strings = reader.readNext()) != null) {
                Product product = productDAO.getProductByName(strings[1]);
                if (!shop.products.containsKey(product)) {
                    writer.writeNext(strings);
                    continue;
                }
                if (shop.products.get(product).getStat() == DEFAULT) {
                    writer.writeNext(strings);
                    continue;
                }
                if (shop.products.get(product).getStat() == CREATED) {
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(strings));
                    list.add(String.valueOf(shop.id));
                    list.add(String.valueOf(shop.products.get(product).getCount()));
                    list.add(String.valueOf(shop.products.get(product).getPrice()));
                    strings = (String[]) list.toArray(new String[list.size()]);
                    writer.writeNext(strings);
                    continue;
                }
                if (shop.products.get(product).getStat() == MODIFIED) {
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(strings));
                    Integer i = 2;
                    while (!i.equals(list.size())) {
                        if (list.get(i).equals(String.valueOf(shop.id))) {
                            Integer.parseInt(list.remove(i + 1));
                            list.add(i + 1, String.valueOf(shop.products.get(product).getCount()));
                            list.remove(i + 2);
                            list.add(i + 2, String.valueOf(shop.products.get(product).getPrice()));
                            break;
                        }
                        i += 3;
                    }
                    strings = (String[]) list.toArray(new String[list.size()]);
                    writer.writeNext(strings);
                    continue;
                }
            }
            writer.close();
            reader.close();
            File file = new File("src/csvFiles/Temp.csv");
            reader = new CSVConstructor("src/csvFiles/Temp.csv").getReader();
            writer = new CSVConstructor("src/csvFiles/Product.csv").getWriter();
            writer.writeAll(reader.readAll());
            writer.close();
            reader.close();
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Shop object) {
        CSVWriter writer = this.constructor.appEnd();
        String[] record = {String.valueOf(object.id), object.name, object.address};
        writer.writeNext(record);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Shop update(Shop object) {
        this.checkProductList(object);
        return object;
    }
}
