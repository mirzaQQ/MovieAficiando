package dk.easv.moviedemo.dal;

import dk.easv.moviedemo.be.Movie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieDAL {

    private final ConnectionManager conMan = new ConnectionManager();

    public void addMovie(String Title, double imbdRating, int catergoryID, String categoryid, String filepath) {

        String sql = """
            INSERT INTO Movie (title, imdbRating, categoryId, filelink)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection con = (Connection) conMan.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, Title);
            pstmt.setDouble(2, imbdRating);
            pstmt.setInt(3, catergoryID);
            pstmt.setString(4, filepath);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not add movie", e);
        }
    }
}
