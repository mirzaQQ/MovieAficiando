package dk.easv.movieaficionado.dal.dao;

import dk.easv.movieaficionado.be.Category;
import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO {

    private final ConnectionManager conMan = new ConnectionManager();

    public void addCategory(String name) throws SQLException {

        String sql = "INSERT INTO Category (name) VALUES (?)";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public Category getCategoryByName(String name) throws SQLException {

        String sql = "SELECT * FROM Category WHERE name = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
