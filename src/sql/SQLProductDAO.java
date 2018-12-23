package sql;

import daos.ProductDAO;
import objects.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLProductDAO implements ProductDAO {
    protected Connection connection = DBConnection.getConnection();

    @Override
    public Integer getID(String name) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select ID from Product where Name = ''?''");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getName(Integer ID) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select Name from Product where ID = ?");
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
            ResultSet resultSet = this.connection.createStatement().executeQuery("select ID from Product order by 1 desc");
            if (resultSet.next()) return resultSet.getInt(1);
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product getProductByName(String name) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select ID, Name from Product where Name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            return new Product(resultSet.getInt(1), resultSet.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product getProductByID(Integer id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select ID, Name from Product where ID = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            return new Product(resultSet.getInt(1), resultSet.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(Product object) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("insert into Product values (?, ?)");
            preparedStatement.setInt(1, object.id);
            preparedStatement.setString(2, object.name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product update(Product object) {
        return null;
    }
}
