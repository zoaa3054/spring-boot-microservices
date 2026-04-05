package com.example.movieinfoservice.resources;

import com.example.movieinfoservice.models.Movie;
import com.example.movieinfoservice.models.MovieSummary;
import com.example.movieinfoservice.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    public MovieResource(RestTemplate restTemplate, MovieRepository movieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
        // Cache lookup is best-effort. If MongoDB is down, continue with source API.
        try {
            Optional<Movie> cachedMovie = movieRepository.findById(movieId);
            if (cachedMovie.isPresent()) {
                return cachedMovie.get();
            }
        } catch (RuntimeException ex) {
            LOGGER.warn("Cache read failed for movieId {}. Falling back to TMDB.", movieId, ex);
        }

        // Source of record is TMDB.
        final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
        MovieSummary movieSummary = restTemplate.getForObject(url, MovieSummary.class);
        if (movieSummary == null) {
            throw new ResponseStatusException(BAD_GATEWAY, "TMDB returned an empty response");
        }

        Movie movie = new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());

        // Cache write is also best-effort; never fail the request because of cache issues.
        try {
            movieRepository.save(movie);
        } catch (RuntimeException ex) {
            LOGGER.warn("Cache write failed for movieId {}. Returning fresh TMDB response.", movieId, ex);
        }

        return movie;
    }
}
