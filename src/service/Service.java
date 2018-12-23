package service;

public interface Service<T> {
    void create(T object);
    T update(T object);
}



//__________________________Deleted______________________________
//    private ProductService productService = new ProductService();
//    private ShopService shopService = new ShopService();
//
//
//    public Service(String property) {
//        if (property.equals("database")) {
//            this.shopDAO = new SQLShopDAO();
//            this.productDAO = new SQLProductDAO();
//        } else {
//            this.shopDAO = new CSVShopDAO();
//            this.productDAO = new CSVProductDAO();
//        }
//    }