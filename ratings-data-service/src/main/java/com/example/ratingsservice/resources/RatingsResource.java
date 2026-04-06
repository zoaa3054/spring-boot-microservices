package com.example.ratingsservice.resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ratingsservice.models.Rating;
import com.example.ratingsservice.models.UserRating;

@RestController
@RequestMapping("/ratings")
public class RatingsResource {

    private final DataSource dataSource;

    public RatingsResource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping("/{userId}")
    public UserRating getRatingsOfUser(@PathVariable String userId) {
        return new UserRating(fetchRatingsByUserId(userId));
    }

    @RequestMapping("/all")
    public List<Rating> getAllRatings() {
        return fetchAllRatings();
    }

    private List<Rating> fetchRatingsByUserId(String userId) {
        try {
            return runQuery(
                    "SELECT movie_id AS movie_id_alias, rating FROM ratings WHERE user_id = ?",
                    userId
            );
        } catch (SQLException ignored) {
        }

        try {
            return runQuery(
                    "SELECT movieId AS movie_id_alias, rating FROM ratings WHERE userId = ?",
                    userId
            );
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to query user ratings from MySQL table 'ratings'.", exception);
        }
    }

    private List<Rating> fetchAllRatings() {
        try {
            return runQuery("SELECT movie_id AS movie_id_alias, rating FROM ratings", null);
        } catch (SQLException ignored) {
        }

        try {
            return runQuery("SELECT movieId AS movie_id_alias, rating FROM ratings", null);
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to query ratings from MySQL table 'ratings'.", exception);
        }
    }

    private List<Rating> runQuery(String sql, String userId) throws SQLException {
        List<Rating> ratings = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (userId != null) {
                statement.setString(1, userId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ratings.add(new Rating(
                            resultSet.getString("movie_id_alias"),
                            resultSet.getInt("rating")
                    ));
                }
            }
        }

        return ratings;
    }
}
