package service;

import com.sun.tools.javac.util.Pair;
import csv.CSVShopDAO;
import daos.ShopDAO;
import objects.Product;
import objects.Shop;
import sql.SQLShopDAO;

import java.util.List;

public class ShopService implements ShopServiceInterface{
    private ShopDAO shopDAO;

    public ShopService(String property){
        if (property.equals("database")) {
            this.shopDAO = new SQLShopDAO();
        } else {
            this.shopDAO = new CSVShopDAO();
        }
    }

    @Override
    public void newShop(String name, String address) {
        Shop shop = new Shop(this.shopDAO.getLastID() + 1, name, address);
        this.shopDAO.create(shop);
    }

    @Override
    public Shop getShopByName(String name){
        return this.shopDAO.getShopByName(name);
    }

    @Override
    public void synchronizeShop(Shop shop){
        this.shopDAO.update(shop);
    }

    @Override
    public Shop findShopWithLowerCost(Product product){
        return this.shopDAO.findShopWithLowerCostForProduct(product);
    }

    @Override
    public Shop findCheapestShop(List<Pair<Product, Integer>> listOfProduct) {
        return this.shopDAO.findCheapestShop(listOfProduct);
    }

    @Override
    public void create(Shop object) {
        this.newShop(object.name, object.address);
    }

    @Override
    public Shop update(Shop object) {
        this.synchronizeShop(object);
        return object;
    }
}