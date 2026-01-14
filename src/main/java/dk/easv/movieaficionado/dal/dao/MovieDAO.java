package dk.easv.movieaficionado.dal.dao;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    private final ConnectionManager conMan = new ConnectionManager();
    CategoryDAO categoryDAO = new CategoryDAO();
    CatMovieDAO catMovieDAO = new CatMovieDAO();

    public List<Movie> getAllMovies() throws SQLException {
        String sql = "SELECT id, name, rating, p_rating, filelink, lastview FROM Movie ORDER BY name";
        List<Movie> movies = new ArrayList<>();

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double rating = rs.getDouble("rating");
                double p_rating = rs.getDouble("p_rating");
                String filelink = rs.getString("filelink");

                Date d = rs.getDate("lastview");
                LocalDate lastview = (d != null) ? d.toLocalDate() : null;

                movies.add(new Movie(
                        id,
                        name,
                        rating,
                        p_rating,
                        filelink,
                        lastview
                ));
            }
        }
        return movies;
    }

    public void addMovie(String title, double imdbRating, String filepath, double p_rating, String category) throws SQLException {

        try (Connection con = conMan.getConnection()) {

            String sql = "INSERT INTO Movie (name, rating, filelink, p_rating) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, title);
                ps.setDouble(2, imdbRating);
                ps.setString(3, filepath);
                ps.setDouble(4, p_rating);

                ps.executeUpdate();

                int movieId;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        movieId = keys.getInt(1);
                    } else {
                        throw new SQLException("No generated key returned for inserted movie.");
                    }
                }

                int categoryId = categoryDAO.getCategoryId(category);
                catMovieDAO.instertMovie(categoryId, movieId);
            }
        }
    }

    public int getMovieId(String filepath) throws SQLException {
        String sql = "SELECT id FROM Movie WHERE filelink = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, filepath);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                throw new SQLException("Movie not found for filelink: " + filepath);
            }
        }
    }
    public void removeMovie(int id){
        String sql = "DELETE FROM Movie WHERE id = ?";
        try(Connection con = conMan.getConnection();){
            catMovieDAO.removeElement(id);
            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
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
