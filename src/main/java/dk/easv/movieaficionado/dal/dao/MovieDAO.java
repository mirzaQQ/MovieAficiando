package dk.easv.movieaficionado.dal.dao;

import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.*;

public class MovieDAO {

    private final ConnectionManager conMan = new ConnectionManager();

    public void addMovie(String Title, String imdbRating, String filepath, String p_rating, String category) throws SQLException {

        try(Connection con = conMan.getConnection()){
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM Category WHERE name = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int catId = rs.getInt("id");
            ps.close();
            stmt.close();

            String sql2 = "INSERT INTO Movie (name, rating, filelink, p_rating) VALUES (?, ?, ?, ?)";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, Title);
            ps2.setString(2, imdbRating);
            ps2.setString(3, filepath);
            ps2.setString(4, p_rating);
            ps2.executeUpdate();
            ps2.close();

            String sql3 = "SELECT id FROM Movie WHERE filelink = ?";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.setString(1, filepath);
            ResultSet rs3 = ps3.executeQuery();
            rs3.next();
            int movieId = rs3.getInt("id");
            ps3.close();

            String sql4 = "INSERT INTO CatMovie (CategoryId, movieId) VALUES (?, ?)";
            PreparedStatement ps4 = con.prepareStatement(sql4);
            ps4.setInt(1, catId);
            ps4.setInt(2, movieId);
            ps4.executeUpdate();
            ps4.close();

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
