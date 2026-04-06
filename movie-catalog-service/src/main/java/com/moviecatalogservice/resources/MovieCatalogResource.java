package com.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalogservice.models.CatalogItem;
import com.moviecatalogservice.models.Rating;
import com.moviecatalogservice.models.TrendingMovie;
import com.moviecatalogservice.services.MovieInfoService;
import com.moviecatalogservice.services.TrendingGrpcClient;
import com.moviecatalogservice.services.UserRatingService;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final MovieInfoService movieInfoService;

    private final UserRatingService userRatingService;

    private final TrendingGrpcClient trendingGrpcClient;

    public MovieCatalogResource(MovieInfoService movieInfoService,
                                UserRatingService userRatingService,
                                TrendingGrpcClient trendingGrpcClient) {

        this.movieInfoService = movieInfoService;
        this.userRatingService = userRatingService;
        this.trendingGrpcClient = trendingGrpcClient;
    }

    /**
     * Makes a call to MovieInfoService to get movieId, name and description,
     * Makes a call to RatingsService to get ratings
     * Accumulates both data to create a MovieCatalog
     * @param userId
     * @return CatalogItem that contains name, description and rating
     */
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        List<Rating> ratings = userRatingService.getUserRating(userId).getRatings();
        return ratings.stream().map(movieInfoService::getCatalogItem).collect(Collectors.toList());
    }

    @RequestMapping("/trending")
    public List<TrendingMovie> getTopTrendingMovies() {
        return trendingGrpcClient.getTopTrendingMovies();
    }
}
