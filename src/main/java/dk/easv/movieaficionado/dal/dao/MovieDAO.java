package dk.easv.movieaficionado.dal.dao;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.movieaficionado.be.Category;
import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    private ConnectionManager conMan = new ConnectionManager();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private CatMovieDAO catMovieDAO = new CatMovieDAO();

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

                Movie movie = new Movie(
                        id,
                        name,
                        rating,
                        p_rating,
                        filelink,
                        lastview
                );

                // Load categories for this movie
                movie.getCategories().addAll(
                        categoryDAO.getCategoriesForMovie(id)
                );

                movies.add(movie);

            }
        }
        return movies;
    }
    //Add Movie (Multi-Category)
    public void addMovie(Movie movie) throws SQLException {

        try (Connection con = conMan.getConnection()) {

            String sql = """
                INSERT INTO Movie (name, rating, filelink, p_rating)
                VALUES (?, ?, ?, ?)
            """;

            try (PreparedStatement ps =
                         con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, movie.getName());
                ps.setDouble(2, movie.getRating());
                ps.setString(3, movie.getFilelink());
                ps.setDouble(4, movie.getPrating());

                ps.executeUpdate();

                int movieId;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        movieId = keys.getInt(1);
                    } else {
                        throw new SQLException("No generated key returned for movie.");
                    }
                }
                // Insert category relations
                for (Category category : movie.getCategories()) {
                    int categoryId = categoryDAO.getCategoryId(category.getName());
                    catMovieDAO.insertMovie(categoryId, movieId);
                    ;
                }
            }
        }
    }
    //Get movie by filepath
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
    //Remove movie
    public void removeMovie(int id) {
        String sql = "DELETE FROM Movie WHERE id = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Remove junction table entries first
            catMovieDAO.removeElement(id);

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete movie", e);
        }
    }
}
