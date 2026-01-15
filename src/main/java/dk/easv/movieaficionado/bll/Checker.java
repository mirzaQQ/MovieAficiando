package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.dal.dao.CategoryDAO;
import dk.easv.movieaficionado.dal.dao.MovieDAO;
import java.sql.SQLException;
import dk.easv.movieaficionado.be.Category;
import java.util.List;

public class Checker {

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }

    public void deleteCategory(int categoryId) throws SQLException {
        categoryDAO.deleteCategory(categoryId);
    }

    public List<Movie> getAllMovies() throws SQLException {
        return movieDAO.getAllMovies();
    }

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final MovieDAO movieDAO = new MovieDAO();

    // Category logic
    public void addCategory(String category) throws SQLException {
        if (categoryDAO.exists(category)) {
            throw new SQLException("Category already exists");
        }
        categoryDAO.addCategory(category);
    }

   //Movie logic
   public void addMovie(Movie movie) throws SQLException {
       movieDAO.addMovie(movie);
   }



    public void removeMovie(int movieId) throws SQLException {
        movieDAO.removeMovie(movieId);
    }


    @Deprecated
    public void addMovie(String title,
                         double imdb,
                         String filepath,
                         double personalRating,
                         String category) {

        throw new UnsupportedOperationException(
                "Use addMovie(Movie) instead – supports multiple categories"
        );
    }
    public void updateMovie(Movie movie) throws SQLException {
        movieDAO.updateMovie(movie);
    }

}
