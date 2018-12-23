package sql;

import service.ProjectProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    public static Connection getConnection(){
        Properties properties = ProjectProperties.loadProperties();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionString = "jdbc:sqlserver://" + properties.getProperty("serverName") +
                    ";databasename=" + properties.getProperty("databaseName");
            return DriverManager.getConnection(connectionString, properties.getProperty("userName"), properties.getProperty("password"));
        } catch (Exception e) {
            System.out.println("Connection database error");
            return null;
        }
    }
}
