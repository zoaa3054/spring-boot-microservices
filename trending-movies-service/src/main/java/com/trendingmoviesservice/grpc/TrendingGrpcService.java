package com.trendingmoviesservice.grpc;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.trendingmoviesservice.models.RatingData;
import com.trendingmoviesservice.services.RatingsDataClient;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@Component
public class TrendingGrpcService extends TrendingServiceGrpc.TrendingServiceImplBase {

    private static final int TOP_N = 10;

    private final RatingsDataClient ratingsDataClient;

    public TrendingGrpcService(RatingsDataClient ratingsDataClient) {
        this.ratingsDataClient = ratingsDataClient;
    }

    @Override
    public void getTopTrendingMovies(TopTrendingMoviesRequest request,
                                     StreamObserver<TopTrendingMoviesResponse> responseObserver) {
        try {
            List<RatingData> ratings = ratingsDataClient.getAllRatings();

            Map<String, Double> averageByMovie = ratings.stream()
                    .collect(Collectors.groupingBy(
                            RatingData::getMovieId,
                            Collectors.averagingInt(RatingData::getRating)
                    ));

            List<TrendingMovie> topMovies = averageByMovie.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder())
                            .thenComparing(Map.Entry.comparingByKey()))
                    .limit(TOP_N)
                    .map(entry -> TrendingMovie.newBuilder()
                            .setMovieId(entry.getKey())
                            .setAverageRating(entry.getValue())
                            .build())
                    .collect(Collectors.toList());

            TopTrendingMoviesResponse response = TopTrendingMoviesResponse.newBuilder()
                    .addAllMovies(topMovies)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception exception) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to compute top trending movies")
                    .withCause(exception)
                    .asRuntimeException());
        }
    }
}
