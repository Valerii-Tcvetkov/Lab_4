package sql;

import java.util.List;

import com.sun.tools.javac.util.Pair;
import daos.ShopDAO;
import objects.Product;
import objects.Property;
import objects.Shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static objects.ObjectStat.*;

public class SQLShopDAO implements ShopDAO {
    protected Connection connection = DBConnection.getConnection();

    @Override
    public Integer getID(String name) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select ID from Shop where Name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getInt(1);
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getName(Integer ID) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select Name from Shop where ID = ?");
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer getLastID() {
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery("select ID from Shop order by 1 desc");
            if (resultSet.next()) return resultSet.getInt(1);
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(Shop object) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("insert into Shop values (?, ?, ?)");
            preparedStatement.setInt(1, object.id);
            preparedStatement.setString(2, object.name);
            preparedStatement.setString(3, object.address);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Shop getShopByName(String name) {
        try {
            PreparedStatement shopInfo = this.connection.prepareStatement("select ID, Name, Address from Shop where Name = ?");
            shopInfo.setString(1, name);
            ResultSet resultSet = shopInfo.executeQuery();
            if (!resultSet.next()) return null;
            Shop shop = new Shop(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
            PreparedStatement products = this.connection.prepareStatement(
                    "select ProductID, Count, Price\n" +
                            "from ShopProduct\n" +
                            "where ShopID = ?"
            );
            products.setInt(1, shop.id);
            resultSet = products.executeQuery();
            SQLProductDAO productDAO = new SQLProductDAO();
            while (resultSet.next()) {
                Product product = productDAO.getProductByID(resultSet.getInt(1));
                Property property = new Property(resultSet.getInt(2), resultSet.getDouble(3), DEFAULT);
                shop.addProduct(product, property);
            }
            return shop;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void checkProductList(Shop shop) {
        try {
            Map<Product, Property> orderList = shop.products;
            PreparedStatement addNewProduct = this.connection.prepareStatement(
                    "insert into ShopProduct values (?, ?, ?, ?)");
            PreparedStatement synchronize = this.connection.prepareStatement(
                    "update ShopProduct\n" +
                            "set Count = ?, Price = ?\n" +
                            "where ShopID = ? and ProductID = ?"
            );
            for (Product product : orderList.keySet()) {
                if (orderList.get(product).getStat() == CREATED) {
                    addNewProduct.setInt(1, shop.id);
                    addNewProduct.setInt(2, product.id);
                    addNewProduct.setInt(3, orderList.get(product).getCount());
                    addNewProduct.setDouble(4, orderList.get(product).getPrice());
                    addNewProduct.executeUpdate();
                    orderList.get(product).setStat(DEFAULT);
                } else if (orderList.get(product).getStat() == MODIFIED) {
                    synchronize.setInt(1, orderList.get(product).getCount());
                    synchronize.setDouble(2, orderList.get(product).getPrice());
                    synchronize.setInt(3, shop.id);
                    synchronize.setInt(4, product.id);
                    synchronize.executeUpdate();
                    orderList.get(product).setStat(DEFAULT);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Shop findShopWithLowerCostForProduct(Product product) {
        try {
            PreparedStatement listOfShops = this.connection.prepareStatement(
                    "select Price, ShopID\n" +
                            "from ShopProduct\n" +
                            "where ProductID = ?\n" +
                            "order by 1"
            );
            listOfShops.setInt(1, product.id);
            ResultSet resultSet = listOfShops.executeQuery();
            if (resultSet.next()) return this.getShopByName(this.getName(resultSet.getInt(2)));
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Shop findCheapestShop(List<Pair<Product, Integer>> listOfProduct) {
        try {
            ResultSet listOfShop = this.connection.createStatement().executeQuery("select Name from Shop");
            Double cost = null;
            Shop answer = null;
            while (listOfShop.next() && cost == null) {
                cost = this.getShopByName(listOfShop.getString(1)).getPriceOfProductList(listOfProduct);
                if (cost != null) {
                    answer = this.getShopByName(listOfShop.getString(1));
                    break;
                }
            }
            while (listOfShop.next()) {
                Double tempCost = this.getShopByName(listOfShop.getString(1)).getPriceOfProductList(listOfProduct);
                if (tempCost != null) {
                    if (cost > tempCost) {
                        answer = this.getShopByName(listOfShop.getString(1));
                        cost = tempCost;
                    }
                }
            }
            return answer;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Shop update(Shop object) {
        this.checkProductList(object);
        return object;
    }
}
