package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connector{
    static Connector connector = new Connector();

    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/database?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String MAX_POOL = "250";

    private static String PASSWORD = "";
    public static void setPass(String pass) {PASSWORD = pass;}

    private Connection connection;
    private Properties properties;

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    // connect database
    public Connection connect() {
        if (connection == null)
            try {connection = DriverManager.getConnection(DATABASE_URL, getProperties());}
            catch ( SQLException e) {e.printStackTrace();}
        return connection;
    }

    public static boolean checkConnection(){
        Connector connector = new Connector();
        try {connector.connection = DriverManager.getConnection(DATABASE_URL, connector.getProperties());}
        catch (SQLException e) {e.printStackTrace(); return false;}
        finally {
            try {connector.connection.close();} catch (SQLException e) {e.printStackTrace();}
            connector.connection = null;}
        return true;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void queryWithoutResult(String statement){
        Connection connection = connector.connect();
        try{connection.prepareStatement(statement).execute();} catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
    }
}
