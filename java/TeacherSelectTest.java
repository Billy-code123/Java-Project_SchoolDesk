package Assignment_2026;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherSelectTest {
	
    public static void main(String[] args) {

        String sql = "SELECT * FROM teachers";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("teacher_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int availability = rs.getInt("availability");

                System.out.println(id + " - " + firstName + " " + lastName
                        + " - Availability: " + availability + " hours");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error while reading teachers.");
            System.out.println(e.getMessage());
        }
    }

}
