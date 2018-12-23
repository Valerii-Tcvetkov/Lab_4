package objects;

import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static objects.ObjectStat.*;

public class Shop {
    public final Integer id;
    public String name;
    public String address;
    public Map<Product, Property> products = new HashMap<Product, Property>();

    public Shop(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public void ordering(Product product, Integer count, Double newPrice) {
        if (products.containsKey(product)){
            products.get(product).addCount(count).setPrice(newPrice).setStat(MODIFIED);
            return;
        }
        this.products.put(product, new Property(count, newPrice));
    }

    public void addProduct(Product product, Property property){
        this.products.put(product, property);
    }

    public Double getPriceOfProduct(Product product) {
        return this.products.get(product).getPrice();
    }

    private Double isPossible(Product product, Integer count) {
        if (this.products.containsKey(product)) {
            if (this.products.get(product).getCount() >= count){
                return this.products.get(product).getPrice();
            }
        }
        return null;
    }

    public Double getPriceOfProductList(List<Pair<Product, Integer>> listOfProduct) {
        Double answer = 0.0;
        for (Pair<Product, Integer> pair : listOfProduct) {
            if (this.isPossible(pair.fst, pair.snd) == null) return null;
            answer += this.isPossible(pair.fst, pair.snd) * pair.snd;
        }
        return answer;
    }

    public List<Pair<Product, Integer>> whatCanBuy(Double money) {
        List<Pair<Product, Integer>> list = new ArrayList<Pair<Product, Integer>>();
        for (Product product : this.products.keySet()){
            Double price = this.getPriceOfProduct(product);
            if ((int) (money / price) != 0){
                list.add(new Pair<Product, Integer>(product, (int) (money / price)));
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
