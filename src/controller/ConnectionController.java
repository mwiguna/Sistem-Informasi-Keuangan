package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionController {
    static Connection conn = null;

    public static Connection connector(){
        if(conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite::resource:keuangan.db");
                return conn;
            } catch (Exception e){
                MainController.errorAlert("Database Connection Error", e.toString());
                return null;
            }
        } else return conn;
    }
}
