package service;

import com.sun.tools.javac.util.Pair;
import objects.Product;
import objects.Shop;

import java.util.List;

public interface ShopServiceInterface extends Service<Shop> {
    public void newShop(String name, String address);
    public Shop getShopByName(String name);
    public void synchronizeShop(Shop shop);
    public Shop findShopWithLowerCost(Product product);
    public Shop findCheapestShop(List<Pair<Product, Integer>> listOfProduct);
}
