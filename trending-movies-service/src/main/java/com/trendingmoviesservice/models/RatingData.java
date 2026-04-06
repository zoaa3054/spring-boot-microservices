package com.trendingmoviesservice.models;

public class RatingData {

    private String movieId;
    private int rating;

    public RatingData() {
    }

    public RatingData(String movieId, int rating) {
        this.movieId = movieId;
        this.rating = rating;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
