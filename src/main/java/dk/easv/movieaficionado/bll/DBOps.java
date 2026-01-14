package dk.easv.movieaficionado.bll;

import java.sql.SQLException;

public class DBOps {

    private final Checker checker = new Checker();

    // Deletes a movie safely (with category relations)
    public void removeMovie(int movieId) throws SQLException {
        System.out.println("Removing movie: " + movieId);
        checker.removeMovie(movieId);
    }
}
