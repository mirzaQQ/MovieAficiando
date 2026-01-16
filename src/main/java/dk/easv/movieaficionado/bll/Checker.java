package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.bll.exceptions.MovieCleanupWarningException;
import dk.easv.movieaficionado.dal.dao.CategoryDAO;
import dk.easv.movieaficionado.dal.dao.MovieDAO;
import java.sql.SQLException;
import dk.easv.movieaficionado.be.Category;
import java.util.List;
import java.time.LocalDate;




public class Checker {



    public void checkForCleanupWarning() throws MovieCleanupWarningException, SQLException {

        List<Movie> movies = movieDAO.getAllMovies();
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);

        // Movies not played for 2+ years (ONLY lastview != null)
        List<Movie> oldMovies = movies.stream()
                .filter(m -> m.getLastview() != null && m.getLastview().isBefore(twoYearsAgo))
                .sorted((a, b) -> a.getLastview().compareTo(b.getLastview()))
                .toList();

        boolean hasLowPersonal = movies.stream()
                .anyMatch(m -> m.getPrating() < 6);

        if (!oldMovies.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("Movies last played more than 2 years ago:\n\n");

            for (Movie m : oldMovies) {
                msg.append("• ")
                        .append(m.getName())
                        .append(" (")
                        .append(m.getLastview())
                        .append(")\n");
            }

            if (hasLowPersonal) {
                msg.append("\n");
                msg.append("Don't forget to remove movies with personal rating below 6.");
            }

            throw new MovieCleanupWarningException(msg.toString());
        }

        if (hasLowPersonal) {
            throw new MovieCleanupWarningException(
                    "Don't forget to remove movies with personal rating below 6."
            );
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



    public void updateMovie(Movie movie) throws SQLException {
    movieDAO.updateMovie(movie);
    }
    public void markMovieAsViewed(String filepath) throws SQLException {
        movieDAO.insertDate(filepath);
    }



}
