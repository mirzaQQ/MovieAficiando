package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.bll.exceptions.MovieCleanupWarningException;
import dk.easv.movieaficionado.dal.dao.CategoryDAO;
import dk.easv.movieaficionado.dal.dao.MovieDAO;
import java.sql.SQLException;
import dk.easv.movieaficionado.be.Category;
import java.util.List;
import java.time.LocalDate;
import java.util.List;



public class Checker {

    public void checkForCleanupWarning()
            throws MovieCleanupWarningException, SQLException {

        List<Movie> movies = movieDAO.getAllMovies();
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);

        for (Movie m : movies) {

            if (m.getPrating() < 6 &&
                    (m.getLastview() == null ||
                            m.getLastview().isBefore(twoYearsAgo))) {

                throw new MovieCleanupWarningException(
                        "Remember to delete movies with a personal rating under 6\n" +
                                "And those that have not been opened for more than 2 years."
                );
            }
        }
    }


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

    // sends the search text to the database and returns matching
    public List<Movie> searchMoviesByTitle(String text) throws SQLException {
        return movieDAO.searchByTitle(text);
    }

public void updateMovie(Movie movie) throws SQLException {
    movieDAO.updateMovie(movie);
}



}
