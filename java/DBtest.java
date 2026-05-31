package Assignment_2026;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBtest {
public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/school_secretary_db";
        String username = "root";
        String password = ""; 

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection successful!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            System.out.println(e.getMessage());
        }
    }

}
