package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {

    //added variables for movies
    private String id;
    private String title;
    private String description;
    private List<Genre> genres;

    private int releaseYear;

    private String imgUrl;

    private int lengthInMinutes;

    private List<String> directors;

    private List<String> writers;

    private List<String> mainCast;

    private  double rating;

    public Movie()
    {

    }
    public Movie(String id, String title, String description, List<Genre> genres, int releaseYear,
                 String imgUrl, int lengthInMinutes, List<String> directors, List<String> writers,
                 List<String> mainCast, double rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }

    public String getId()
    {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {return genres;}

    public int getReleaseYear() {
        return releaseYear;
    }
    public String getImgUrl ()
    {
        return imgUrl;
    }

    public int getlengthInMinutes() {
        return lengthInMinutes;
    }
    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }
    public List<String> getMainCast()
    {
        return mainCast;
    }
    public double getRating() {
        return rating;
    }

}