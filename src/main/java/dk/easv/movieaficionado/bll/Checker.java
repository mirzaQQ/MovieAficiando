package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.dal.dao.CategoryDAO;
import dk.easv.movieaficionado.dal.dao.MovieDAO;

import java.sql.SQLException;

public class Checker {
    CategoryDAO categoryDAO = new CategoryDAO();
    MovieDAO movieDAO = new MovieDAO();

    public void addCategory(String category) throws SQLException {
    //Checks if the category already exist before inserting
        if (categoryDAO.exists(category)) {
            throw new SQLException("Category already exists");
        }
        categoryDAO.addCategory(category);

    }

    public void addMovie(String title, double imdb, String filepath, double personal, String category) throws SQLException {
        movieDAO.addMovie(title, imdb, filepath, personal, category);
    }
}


