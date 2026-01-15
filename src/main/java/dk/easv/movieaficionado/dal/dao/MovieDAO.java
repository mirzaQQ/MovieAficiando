package dk.easv.movieaficionado.dal.dao;

import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    private final ConnectionManager conMan = new ConnectionManager();

    // Inserts movie and returns generated movieId
    public int addMovie(String title, double imdbRating, String filepath, double personalRating) throws SQLException {

        String sql = "INSERT INTO Movie (name, rating, filelink, p_rating) VALUES (?, ?, ?, ?)";

        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setDouble(2, imdbRating);
            ps.setString(3, filepath);
            ps.setDouble(4, personalRating);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);   // movieId
            }

            throw new SQLException("No generated key returned");
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
                Date d = rs.getDate("lastview");
                if (d != null) lastview = d.toLocalDate();

                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("rating"),
                        rs.getDouble("p_rating"),
                        rs.getString("filelink"),
                        lastview
                ));
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
        String sql = "DELETE FROM Movie WHERE id = ?";
        try (Connection con = conMan.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
