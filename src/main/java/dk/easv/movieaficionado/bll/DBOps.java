package dk.easv.movieaficionado.bll;

import dk.easv.movieaficionado.be.Movie;
import dk.easv.movieaficionado.dal.dao.MovieDAO;

public class DBOps {
    MovieDAO  movieDAO = new MovieDAO();
    public void removeMovie(int movieId) {
        System.out.println("Removing movie: " + movieId);
        movieDAO.removeMovie(movieId);
    }
}
