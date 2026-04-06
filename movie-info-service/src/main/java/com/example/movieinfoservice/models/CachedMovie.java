package com.example.movieinfoservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cached_movies")
public class CachedMovie {

    @Id
    private String id;

    @Indexed(unique = true)
    private String movieId;

    private String title;
    private String description;
    private long cachedAt;

    public CachedMovie() {
    }

    public CachedMovie(String movieId, String title, String description) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.cachedAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCachedAt() {
        return cachedAt;
    }

    public void setCachedAt(long cachedAt) {
        this.cachedAt = cachedAt;
    }
}
