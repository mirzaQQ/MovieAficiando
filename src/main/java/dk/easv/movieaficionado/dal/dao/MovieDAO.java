package dk.easv.movieaficionado.dal.dao;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.movieaficionado.dal.ConnectionManager;

import java.sql.*;

public class MovieDAO {

    private final ConnectionManager conMan = new ConnectionManager();
    CategoryDAO categoryDAO = new CategoryDAO();
    CatMovieDAO catMovieDAO = new CatMovieDAO();
    public void addMovie(String Title, String imdbRating, String filepath, String p_rating, String category) throws SQLException {

        try(Connection con = conMan.getConnection()){

            String sql2 = "INSERT INTO Movie (name, rating, filelink, p_rating) VALUES (?, ?, ?, ?)";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, Title);
            ps2.setString(2, imdbRating);
            ps2.setString(3, filepath);
            ps2.setString(4, p_rating);
            ps2.executeUpdate();
            ps2.close();


            catMovieDAO.instertMovie(categoryDAO.getCategoryId(category), getMovieId(filepath));

        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
    public int getMovieId(String Filepath){
        String sql = "SELECT id FROM Movie WHERE filelink = ?";
        try(Connection con = conMan.getConnection();){
            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, Filepath);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int movieId = rs.getInt("id");
            ps.close();
            stmt.close();
            return movieId;

        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
