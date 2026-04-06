package com.trendingmoviesservice.services;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendingRatingsQueryService {

    private final JdbcTemplate jdbcTemplate;

    public TrendingRatingsQueryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TrendingResultRow> getTopTrendingMovies(int limit) {
        // Preferred query form from requirements where DB performs AVG + sort + limit.
        try {
            return jdbcTemplate.query(
                    "SELECT CAST(movieId AS CHAR) AS movie_id, AVG(rating) AS average_rating " +
                            "FROM ratings GROUP BY movieId ORDER BY average_rating DESC, movieId ASC LIMIT ?",
                    (resultSet, rowNum) -> new TrendingResultRow(
                            resultSet.getString("movie_id"),
                            resultSet.getDouble("average_rating")
                    ),
                    limit
            );
        } catch (DataAccessException ignored) {
            // Fallback for schemas that use snake_case columns.
            return jdbcTemplate.query(
                    "SELECT CAST(movie_id AS CHAR) AS movie_id, AVG(rating) AS average_rating " +
                            "FROM ratings GROUP BY movie_id ORDER BY average_rating DESC, movie_id ASC LIMIT ?",
                    (resultSet, rowNum) -> new TrendingResultRow(
                            resultSet.getString("movie_id"),
                            resultSet.getDouble("average_rating")
                    ),
                    limit
            );
        }
    }

    public static class TrendingResultRow {
        private final String movieId;
        private final double averageRating;

        public TrendingResultRow(String movieId, double averageRating) {
            this.movieId = movieId;
            this.averageRating = averageRating;
        }

        public String getMovieId() {
            return movieId;
        }

        public double getAverageRating() {
            return averageRating;
        }
    }
}
