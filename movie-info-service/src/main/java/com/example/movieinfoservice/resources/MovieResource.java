package com.example.movieinfoservice.resources;

import com.example.movieinfoservice.models.CachedMovie;
import com.example.movieinfoservice.models.Movie;
import com.example.movieinfoservice.models.MovieSummary;
import com.example.movieinfoservice.repositories.MovieCacheRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieCacheRepository movieCacheRepository;

    public MovieResource(RestTemplate restTemplate, MovieCacheRepository movieCacheRepository) {
        this.restTemplate = restTemplate;
        this.movieCacheRepository = movieCacheRepository;
    }

    @RequestMapping("/{movieId}/{cacheEnabled}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId, @PathVariable("cacheEnabled") boolean cacheEnabled) {
        if (cacheEnabled) {
            return movieCacheRepository.findByMovieId(movieId)
                    .map(cached -> new Movie(cached.getMovieId(), cached.getTitle(), cached.getDescription()))
                    .orElseGet(() -> fetchMovieFromTMDB(movieId, true));
        } else {
            return fetchMovieFromTMDB(movieId, false);
        }
    }

    private Movie fetchMovieFromTMDB(String movieId, boolean saveToCache) {
        final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
        MovieSummary movieSummary = restTemplate.getForObject(url, MovieSummary.class);

        if (movieSummary == null) {
            return new Movie(movieId, "Unknown", "No description available.");
        }

        Movie movie = new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
        if (saveToCache) {
            try {
                movieCacheRepository.save(new CachedMovie(movieId, movie.getName(), movie.getDescription()));
            } catch (com.mongodb.MongoWriteException | org.springframework.dao.DuplicateKeyException e) {
                // Ignore duplicate key errors - it just means another thread cached the movie first
            }
        }
        return movie;
    }
}
