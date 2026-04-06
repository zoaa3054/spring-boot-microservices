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

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieResource.class);

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieCacheRepository movieCacheRepository;

    public MovieResource(RestTemplate restTemplate, MovieCacheRepository movieCacheRepository) {
        this.restTemplate = restTemplate;
        this.movieCacheRepository = movieCacheRepository;
    }

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
        return movieCacheRepository.findByMovieId(movieId)
                .map(cached -> new Movie(cached.getMovieId(), cached.getTitle(), cached.getDescription()))
                .orElseGet(() -> {
                    final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
                    MovieSummary movieSummary = restTemplate.getForObject(url, MovieSummary.class);

                    if (movieSummary == null) {
                        return new Movie(movieId, "Unknown", "No description available.");
                    }

                    Movie movie = new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
                    movieCacheRepository.save(new CachedMovie(movieId, movie.getName(), movie.getDescription()));
                    return movie;
                });
    }
}
