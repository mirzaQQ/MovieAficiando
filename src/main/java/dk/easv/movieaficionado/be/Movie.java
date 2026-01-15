package dk.easv.movieaficionado.be;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Movie {
    private int id;
    private String name;
    private double rating;
    private double p_rating;
    private String filelink;
    private LocalDate lastview;

    private Set<Category> categories = new HashSet<>();

    //Constructor from the database
    public  Movie(int id, String name, double rating, double p_rating, String filelink, LocalDate lastview) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.p_rating = p_rating;
        this.filelink = filelink;
        this.lastview = lastview;

    }
    //Constructor for new movies
    public Movie(String name, String filelink, Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("Movie must have at least one category");
        }
        this.name = name;
        this.filelink = filelink;
        this.categories.addAll(categories);
    }

    public Movie(int id, String name, String filelink, Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("Movie must have at least one category");
        }
        this.id = id;
        this.name = name;
        this.filelink = filelink;
        this.categories.addAll(categories);
    }


    // Category handling
    public Set<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        if (categories.size() == 1) {
            throw new IllegalStateException("Movie must have at least one category");
        }
        categories.remove(category);
    }

    // existing getters/setters unchanged
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getRating() {
        return rating;
    }
    public double getPrating() {
        return p_rating;
    }
    public String getFilelink() {
        return filelink;
    }
    public LocalDate getLastview() {
        return lastview;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setPrating(double p_rating) {
        this.p_rating = p_rating;
    }
    public void setFilelink(String filelink) {
        this.filelink = filelink;
    }
    public void setLastview(LocalDate lastview) {
        this.lastview = lastview;
    }
    @Override
    public String toString() {
        return name;
    }
}
