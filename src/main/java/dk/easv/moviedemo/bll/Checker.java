package dk.easv.moviedemo.bll;

import dk.easv.moviedemo.dal.dao.CategoryDAO;

import java.sql.SQLException;


public class Checker {
    CategoryDAO categoryDAO = new CategoryDAO();
    public void addCategory(String category) throws SQLException {
        /**In this we should create the logic that can check whether
         * the category exits or not. For that we will need another method in CategoryDAL
         */
        categoryDAO.addCategory(category);
    }
}
