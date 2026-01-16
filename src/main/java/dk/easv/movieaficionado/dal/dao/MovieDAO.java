package dk.easv.movieaficionado.dal.dao;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.movieaficionado.be.Category;
import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class MovieDAO {

    private final ConnectionManager conMan = new ConnectionManager();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final CatMovieDAO catMovieDAO = new CatMovieDAO();

    public void addMovie(Movie movie) throws SQLException {

        if (movie.getCategories().isEmpty()) {
            throw new IllegalArgumentException("Movie must have at least one category");
        }

        String sql = "INSERT INTO Movie (name, rating, filelink, p_rating) VALUES (?, ?, ?, ?)";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps =
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
                    throw new SQLException("No generated key returned for movie");
                }
            }

            // Insert category relations
            for (Category category : movie.getCategories()) {
                int categoryId = categoryDAO.getCategoryId(category.getName());
                catMovieDAO.insertMovie(categoryId, movieId);
            }
        }
    }

    @Deprecated
    public int addMovie(String title,
                        double imdbRating,
                        String filepath,
                        double personalRating) throws SQLException {

        String sql = "INSERT INTO Movie (name, rating, filelink, p_rating) VALUES (?, ?, ?, ?)";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setDouble(2, imdbRating);
            ps.setString(3, filepath);
            ps.setDouble(4, personalRating);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException("No generated key returned");
        }
    }

    public void updateMovie(Movie movie) throws SQLException {

        if (movie.getCategories().isEmpty()) {
            throw new IllegalArgumentException("Movie must have at least one category");
        }

        String sql = "UPDATE Movie SET name = ?, rating = ?, filelink = ?, p_rating = ? WHERE id = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, movie.getName());
            ps.setDouble(2, movie.getRating());
            ps.setString(3, movie.getFilelink());
            ps.setDouble(4, movie.getPrating());
            ps.setInt(5, movie.getId());
            ps.executeUpdate();

            // přepíše vazby kategorií (reset + znovu vložit)
            catMovieDAO.removeElement(movie.getId());

            for (Category category : movie.getCategories()) {
                int categoryId = categoryDAO.getCategoryId(category.getName());
                catMovieDAO.insertMovie(categoryId, movie.getId());
            }
        }
    }


    public List<Movie> getAllMovies() throws SQLException {

        String sql = "SELECT id, name, rating, p_rating, filelink, lastview FROM Movie ORDER BY name";
        List<Movie> movies = new ArrayList<>();


        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                LocalDate lastview = null;
                Timestamp ts = rs.getTimestamp("lastview");
                if (ts != null) {
                    lastview = ts.toLocalDateTime().toLocalDate();
                }

                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("rating"),
                        rs.getDouble("p_rating"),
                        rs.getString("filelink"),
                        lastview
                );

                // load categories
                movie.getCategories().addAll(
                        categoryDAO.getCategoriesForMovie(movie.getId())
                );

                movies.add(movie);
            }
        }
        return movies;
    }

    public List<Movie> getMoviesLastViewedBefore(LocalDateTime cutoff) throws SQLException {
        String sql = """
        SELECT id, name, rating, p_rating, filelink, lastview
        FROM Movie
        WHERE lastview IS NOT NULL
          AND lastview < ?
        ORDER BY lastview ASC
    """;

        List<Movie> movies = new ArrayList<>();

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(cutoff));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate lastview = null;
                    Timestamp ts = rs.getTimestamp("lastview");
                    if (ts != null) {
                        lastview = ts.toLocalDateTime().toLocalDate();
                    }

                    Movie movie = new Movie(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("rating"),
                            rs.getDouble("p_rating"),
                            rs.getString("filelink"),
                            lastview
                    );

                    movie.getCategories().addAll(
                            categoryDAO.getCategoriesForMovie(movie.getId())
                    );

                    movies.add(movie);
                }
            }
        }
        return movies;
    }
    public int getMovieId(String filepath) throws SQLException {

        String sql = "SELECT id FROM Movie WHERE filelink = ?";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, filepath);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");
        }
    }


    public void removeMovie(int id) throws SQLException {

        // remove relations first
        catMovieDAO.removeElement(id);

        String sql = "DELETE FROM Movie WHERE id = ?";
        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    public List<Movie>searchByTitle(String query) throws SQLException {

        String sql = """
                SELECT id, name, rating, p_rating, filelink, lastview
                FROM Movie Where name LIKE ?
                ORDER BY name
                """;
        List<Movie> movies = new ArrayList<>();
            try (Connection con = conMan.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + query.trim() + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    LocalDate lastview = null;
                    Timestamp ts = rs.getTimestamp("lastview");
                    if (ts != null) {
                        lastview = ts.toLocalDateTime().toLocalDate();
                    }

                    Movie movie = new Movie(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("rating"),
                            rs.getDouble("p_rating"),
                            rs.getString("filelink"),
                            lastview
                    );

                    movie.getCategories().addAll(
                            categoryDAO.getCategoriesForMovie(movie.getId())
                    );
                    movies.add(movie);
                }
            }
            return movies;
    }
    public void insertDate(String filepath){
        LocalDateTime now = LocalDateTime.now();

        String sql = "UPDATE Movie SET lastview = ? WHERE filelink = ?";
        try(Connection con = conMan.getConnection();){
            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, now);
            ps.setString(2, filepath);
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
