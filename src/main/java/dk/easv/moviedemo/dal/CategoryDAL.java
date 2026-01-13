package dk.easv.moviedemo.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

public class CategoryDAL {

    ConnectionManager conMan = new ConnectionManager();

    //checks if a category with this name already exist
    public boolean checkCategory(String categoryName) throws SQLException {
        String sql = "SELECT * FROM Category WHERE name = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        }
    }

    //Adds a new category if it does not already exist
    public void addCategory(String category) throws SQLException {
        if (checkCategory(category)) {
            throw new SQLException("Category already exists");
        }
        String sql = "INSERT INTO Category (name) VALUES (?)";
        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.executeUpdate();
        }
    }
}




