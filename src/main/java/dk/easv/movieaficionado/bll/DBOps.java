package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.be.Movie;

import java.sql.SQLException;

import java.util.List;


public class DBOps {

    private final Checker checker = new Checker();

    // Deletes a movie safely (with category relations)
    public void removeMovie(int movieId) throws SQLException {
        System.out.println("Removing movie: " + movieId);
        checker.removeMovie(movieId);
    }

    public List<Movie> searchMoviesByTitle(String text) throws SQLException {
        return checker.searchMoviesByTitle(text);
    }
}
