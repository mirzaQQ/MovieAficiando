package dk.easv.moviedemo.be;

public class Movie {
    private int id;
    private String name;
    private int rating;
    private String filelink;
    private String lastview;

    public  Movie(int id, String name, String filelink) {
        this.id = id;
        this.name = name;
        this.filelink = filelink;
    }
    public Movie(String name, String filelink) {
        this.name = name;
        this.filelink = filelink;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getRating() {
        return rating;
    }
    public String getFilelink() {
        return filelink;
    }
    public String getLastview() {
        return lastview;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setFilelink(String filelink) {
        this.filelink = filelink;
    }
    public void setLastview(String lastview) {
        this.lastview = lastview;
    }
}
