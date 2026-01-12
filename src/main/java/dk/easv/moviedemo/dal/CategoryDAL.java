package dk.easv.moviedemo.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoryDAL {
    public void addCategory(String category) {
        ConnectionManager conMan = new ConnectionManager();
        try(Connection con = conMan.getConnection()) {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO Category (name) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, category);
            pstmt.executeUpdate();
            pstmt.close();
            stmt.close();


        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
