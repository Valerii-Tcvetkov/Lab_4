package daos;

import java.util.List;
import com.sun.tools.javac.util.Pair;
import objects.Product;
import objects.Shop;

public interface ShopDAO extends DAO<Shop>{
    public Integer getID(String name);
    public String getName(Integer ID);
    public Integer getLastID();
    public Shop getShopByName(String name);
    public void checkProductList(Shop shop);
    public Shop findShopWithLowerCostForProduct(Product product);
    public Shop findCheapestShop(List<Pair<Product, Integer>> listOfProduct);
}
