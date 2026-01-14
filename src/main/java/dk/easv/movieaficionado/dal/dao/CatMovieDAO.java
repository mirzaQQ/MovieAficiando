package dk.easv.movieaficionado.dal.dao;

import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CatMovieDAO {

    ConnectionManager conMan = new ConnectionManager();

    //checks if a category with this name already exist
    public boolean checkCategory(String categoryName) throws SQLException {
        String sql = "SELECT FROM Category WHERE name = ?";

        try (Connection con = (Connection) conMan.getConnection();
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
        try (Connection con = (Connection) conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.executeUpdate();
        }
    }
}