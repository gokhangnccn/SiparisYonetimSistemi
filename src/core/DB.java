package core;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    //singleton design pattern
    private static DB instance = null;
    private Connection con = null;
    private final String DB_URL = "jdbc:postgresql://localhost:5432/customerManage";
    private final String DB_USERNAME = "postgres";
    private final String DB_PASSWORD = "4245";

    private DB(){
        try {
            this.con = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getCon() {
        return con;
    }

    public static Connection getInstance(){
        try {
            if(instance == null || instance.getCon().isClosed()){
                instance = new DB();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instance.getCon();
    }
}
