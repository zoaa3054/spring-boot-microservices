package com.moviecatalogservice.models;

public class TrendingMovie {

    private String movieId;
    private double averageRating;

    public TrendingMovie() {
    }

    public TrendingMovie(String movieId, double averageRating) {
        this.movieId = movieId;
        this.averageRating = averageRating;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
