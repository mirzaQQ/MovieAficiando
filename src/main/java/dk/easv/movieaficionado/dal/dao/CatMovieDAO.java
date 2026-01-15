package dk.easv.movieaficionado.dal.dao;

import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CatMovieDAO {

    private final ConnectionManager conMan = new ConnectionManager();

    // INSERT relation between category and movie
    public void insertMovie(int categoryId, int movieId) {
        String sql = "INSERT INTO CatMovie (CategoryId, movieId) VALUES (?, ?)";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ps.setInt(2, movieId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert CatMovie relation", e);
        }
    }

    // DELETE all category relations for a movie
    public void removeElement(int movieId) {
        String sql = "DELETE FROM CatMovie WHERE movieId = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete CatMovie relations", e);
        }
    }
}
