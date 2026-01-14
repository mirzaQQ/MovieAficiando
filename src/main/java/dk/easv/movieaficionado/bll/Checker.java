package dk.easv.movieaficionado.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.movieaficionado.dal.dao.CategoryDAO;
import dk.easv.movieaficionado.dal.dao.MovieDAO;

import java.sql.SQLException;

public class Checker {
    CategoryDAO categoryDAL = new CategoryDAO();
    MovieDAO movieDAO = new MovieDAO();

    public void addCategory(String category) throws SQLException {
        /**
         * In this we should create the logic that can check whether
         * the category exits or not. For that we will need another method in CategoryDAL
         */
        categoryDAL.addCategory(category);

    }

    public void addMovie(String title, double imdb, String filepath, double personal, String category) throws SQLException {
        movieDAO.addMovie(title, imdb, filepath, personal, category);
    }
}


