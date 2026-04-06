package com.trendingmoviesservice.grpc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.trendingmoviesservice.services.TrendingRatingsQueryService;
import com.trendingmoviesservice.services.TrendingRatingsQueryService.TrendingResultRow;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@Component
public class TrendingGrpcService extends TrendingServiceGrpc.TrendingServiceImplBase {

    private static final int TOP_N = 10;

        private final TrendingRatingsQueryService trendingRatingsQueryService;

        public TrendingGrpcService(TrendingRatingsQueryService trendingRatingsQueryService) {
                this.trendingRatingsQueryService = trendingRatingsQueryService;
    }

    @Override
    public void getTopTrendingMovies(TopTrendingMoviesRequest request,
                                     StreamObserver<TopTrendingMoviesResponse> responseObserver) {
        try {
            List<TrendingResultRow> queryRows = trendingRatingsQueryService.getTopTrendingMovies(TOP_N);

            List<TrendingMovie> topMovies = queryRows.stream()
                    .limit(TOP_N)
                    .map(row -> TrendingMovie.newBuilder()
                            .setMovieId(row.getMovieId())
                            .setAverageRating(row.getAverageRating())
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
