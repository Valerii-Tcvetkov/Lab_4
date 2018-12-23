package service;

import csv.CSVProductDAO;
import daos.ProductDAO;
import objects.Product;
import sql.SQLProductDAO;

public class ProductService implements ProductServiceInterface{
    private ProductDAO productDAO;

    public ProductService(String property){
        if (property.equals("database")) {
            this.productDAO = new SQLProductDAO();
        } else {
            this.productDAO = new CSVProductDAO();
        }
    }

    @Override
    public void newProduct(String name) {
        Product product = new Product(this.productDAO.getLastID() + 1, name);
        this.productDAO.create(product);
    }

    @Override
    public Product getProductByName(String name) {
        return this.productDAO.getProductByName(name);
    }

    @Override
    public void create(Product object) {
        this.newProduct(object.name);
    }

    @Override
    public Product update(Product object) {
        return null;
    }
}
