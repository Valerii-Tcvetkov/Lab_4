import com.sun.tools.javac.util.Pair;
import objects.Product;
import objects.Shop;

import service.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties properties = ProjectProperties.loadProperties();

        ProductService productService = new ProductService(properties.getProperty("data"));
        ShopService shopService = new ShopService(properties.getProperty("data"));
        Shop shop = shopService.getShopByName("Дикси");
        Product product1 = productService.getProductByName("Шоколад");
        Product product2 = productService.getProductByName("Молоко");


//        Shop shop = shopService.getShopByName("Дикси");
//        shop.ordering(productService.getProductByName("Молоко"), 20, 59.5);
//
//        shopService.synchronizeShop(shop);
//
//
//        productService.newProduct("Шоколад");
//        productService.newProduct("Молоко");
//        shopService.newShop("Ашан", "ул. Петрова 7");
//        shopService.newShop("Дикси", "ул. Петрова 8");
//        shopService.newShop("Лента", "ул. Петрова 9");
//
//        Product product1 = productService.getProductByName("Молоко");
//        Product product2 = productService.getProductByName("Шоколад");
//        Shop shop = shopService.getShopByName("Ашан");
//
//        shop.ordering(product1, 50, 69.0);
//        shop.ordering(product2, 100, 39.99);
//
//        shopService.synchronizeShop(shop);
//
//        System.out.println(shopService.findShopWithLowerCost(product2));
//
//        ArrayList<Pair<Product, Integer>> list = (ArrayList<Pair<Product, Integer>>) shop.whatCanBuy(300.0);
//        for (Pair<Product, Integer> pair : list){
//            System.out.println(pair.fst.name + "   " + pair.snd);
//        }

        ArrayList<Pair<Product, Integer>> listToBuy = new ArrayList<>();
        listToBuy.add(new Pair<>(product1, 1));
        listToBuy.add(new Pair<>(product2, 100));
//        System.out.println(shop.getPriceOfProductList(listToBuy));
        System.out.println(shopService.findCheapestShop(listToBuy));

    }
}