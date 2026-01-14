package dk.easv.movieaficionado.dal.dao;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.*;

public class CatMovieDAO {
    private final ConnectionManager conMan = new ConnectionManager();

    public void instertMovie(int catId, int movieId) throws SQLServerException {
        String sql = "INSERT INTO CatMovie (CategoryId, movieId) VALUES (?, ?)";
        try (Connection con = conMan.getConnection();) {
            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, catId);
            ps.setInt(2, movieId);
            ps.executeUpdate();
            ps.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void removeElement(int movieId) {
        String sql = "DELETE FROM CatMovie WHERE movieId = ?";
        try(Connection con = conMan.getConnection();) {
            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, movieId);
            ps.executeUpdate();
            ps.close();
            stmt.close();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}