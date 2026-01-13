package dk.easv.movieaficionado.dal.dao;

import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieDAO {

    private final ConnectionManager conMan = new ConnectionManager();

    public void addMovie(String Title, double imbdRating, double personalRating, int catergoryID, String categoryID, String filepath) {

        String sql = """
            INSERT INTO Movie (title, imdbRating, personalRating, categoryId, filelink)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection con = (Connection) conMan.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, Title);
            pstmt.setDouble(2, imbdRating);
            pstmt.setDouble(3, personalRating);
            pstmt.setInt(4, catergoryID);
            pstmt.setString(5, filepath);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not add movie", e);
        }
    }

}
