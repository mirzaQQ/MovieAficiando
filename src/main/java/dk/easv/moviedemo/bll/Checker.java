package dk.easv.moviedemo.bll;

import dk.easv.moviedemo.dal.CategoryDAL;
import java.sql.SQLException;


public class Checker {
    CategoryDAL categoryDAL = new CategoryDAL();
    public void addCategory(String category) throws SQLException {
        /**In this we should create the logic that can check whether
         * the category exits or not. For that we will need another method in CategoryDAL
         */
        categoryDAL.addCategory(category);
    }
}
