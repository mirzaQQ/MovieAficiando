package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.dal.dao.CategoryDAO;
import dk.easv.movieaficionado.dal.dao.MovieDAO;

import java.sql.SQLException;

public class Checker {

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final MovieDAO movieDAO = new MovieDAO();

    public void addCategory(String category) throws SQLException {
        // Checks if the category already exists before inserting
        if (categoryDAO.exists(category)) {
            throw new SQLException("Category already exists");
        }
        categoryDAO.addCategory(category);
    }

    public void addMovie(Movie movie) throws SQLException {
        movieDAO.addMovie(movie);
    }
}
