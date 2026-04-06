package com.trendingmoviesservice.services;

import com.trendingmoviesservice.models.RatingData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class RatingsDataClient {

    private final RestTemplate restTemplate;
    private final String ratingsServiceBaseUrl;

    public RatingsDataClient(@Value("${ratings.service.base-url:http://localhost:8083}") String ratingsServiceBaseUrl) {
        this.restTemplate = new RestTemplate();
        this.ratingsServiceBaseUrl = ratingsServiceBaseUrl;
    }

    public List<RatingData> getAllRatings() {
        ResponseEntity<List<RatingData>> response = restTemplate.exchange(
                ratingsServiceBaseUrl + "/ratings/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RatingData>>() {
                }
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return response.getBody();
    }
}
