package service;

import objects.Product;

public interface ProductServiceInterface extends Service<Product>{
    public void newProduct(String name);
    public Product getProductByName(String name);
}
