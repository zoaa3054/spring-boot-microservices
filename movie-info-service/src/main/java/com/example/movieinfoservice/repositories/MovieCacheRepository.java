package com.example.movieinfoservice.repositories;

import com.example.movieinfoservice.models.CachedMovie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MovieCacheRepository extends MongoRepository<CachedMovie, String> {
    Optional<CachedMovie> findByMovieId(String movieId);
}
