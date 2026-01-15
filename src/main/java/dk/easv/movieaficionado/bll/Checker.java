package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.dal.dao.CategoryDAO;
import dk.easv.movieaficionado.dal.dao.MovieDAO;
import dk.easv.movieaficionado.dal.dao.CatMovieDAO;

import java.sql.SQLException;

public class Checker {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final MovieDAO movieDAO = new MovieDAO();
    private final CatMovieDAO catMovieDAO = new CatMovieDAO();

    public void addCategory(String category) throws SQLException {
    //Checks if the category already exist before inserting
        if (categoryDAO.exists(category)) {
            throw new SQLException("Category already exists");
        }
        categoryDAO.addCategory(category);

    }
    // Adds movie and connects it to a category
    public void addMovie(String title, double imdbRating, String filepath,
                         double personalRating, String category) throws SQLException {

        //Saves movie and get its ID
        int movieId = movieDAO.addMovie(title, imdbRating, filepath, personalRating);

        //Find category ID
        int categoryId = categoryDAO.getCategoryId(category);

        //Connect movie & category
        catMovieDAO.insertMovie(categoryId, movieId);
    }
    public void removeMovie(int movieId) throws SQLException {

        // First delete all category relations for this movie
        catMovieDAO.removeElement(movieId);

        // Then delete the movie itself
        movieDAO.removeMovie(movieId);
    }
}



