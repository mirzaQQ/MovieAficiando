package dk.easv.moviedemo.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

public class CategoryDAL {
    ConnectionManager conMan = new ConnectionManager();
    public void addCategory(String category) {

        try(Connection con = (Connection) conMan.getConnection()) {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO Category (name) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, category);
            pstmt.executeUpdate();
            pstmt.close();
            stmt.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
