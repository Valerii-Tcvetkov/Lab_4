package daos;

import objects.Product;

public interface ProductDAO extends DAO<Product>{
    public Integer getID(String name);
    public String getName(Integer ID);
    public Integer getLastID();
    public Product getProductByName(String name);
    public Product getProductByID(Integer id);
}
