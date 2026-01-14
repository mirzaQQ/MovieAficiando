package dk.easv.movieaficionado.dal.dao;

import dk.easv.movieaficionado.be.Category;
import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public int getCategoryId(String name) throws SQLException {
        try (Connection con = conMan.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT id FROM Category WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int categoryId = rs.getInt("id");
            return categoryId;
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

    public List<Category> getAllCategories() throws SQLException {
        String sql = "SELECT * FROM Category ORDER BY name";
        List<Category> categories = new ArrayList<>();
        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            return categories;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM Category WHERE id = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException(e);


        }
    }
    // Returns true if a category with this name already exists
    public boolean exists(String name) throws SQLException {

        String sql = "SELECT 1 FROM Category WHERE name = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        }
    }

}
