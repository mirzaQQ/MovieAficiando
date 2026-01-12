package dk.easv.moviedemo.be;

public class CatMovie {
    private int id;
    private int CategoryId;
    private int  MovieId;

    public CatMovie(int id, int CategoryId, int MovieId) {
        this.id = id;
        this.CategoryId = CategoryId;
        this.MovieId = MovieId;
    }


    public int getId() {
        return id;
    }
    public int getCategoryId() {
        return CategoryId;
    }
    public int getMovieId() {
        return MovieId;
    }
}
