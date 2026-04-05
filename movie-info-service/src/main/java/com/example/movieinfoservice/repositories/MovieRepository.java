package com.example.movieinfoservice.repositories;

import com.example.movieinfoservice.models.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}
