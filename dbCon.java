package dbCon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class dbCon {
    private static Connection connection = null;
    public static Connection getConnection()  {

        try {
            Class.forName("org.sqllite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:accountsDb.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
