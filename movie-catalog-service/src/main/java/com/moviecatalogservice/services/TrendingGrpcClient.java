package com.moviecatalogservice.services;

import com.moviecatalogservice.models.TrendingMovie;
import com.trendingmoviesservice.grpc.TopTrendingMoviesRequest;
import com.trendingmoviesservice.grpc.TopTrendingMoviesResponse;
import com.trendingmoviesservice.grpc.TrendingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TrendingGrpcClient implements InitializingBean, DisposableBean {

    private final String host;
    private final int port;

    private ManagedChannel channel;
    private TrendingServiceGrpc.TrendingServiceBlockingStub blockingStub;

    public TrendingGrpcClient(@Value("${trending.grpc.host:localhost}") String host,
                              @Value("${trending.grpc.port:9090}") int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = TrendingServiceGrpc.newBlockingStub(channel);
    }

    public List<TrendingMovie> getTopTrendingMovies() {
        TopTrendingMoviesResponse response = blockingStub.getTopTrendingMovies(
                TopTrendingMoviesRequest.newBuilder().build()
        );

        return response.getMoviesList().stream()
                .map(movie -> new TrendingMovie(movie.getMovieId(), movie.getAverageRating()))
                .collect(Collectors.toList());
    }

    @Override
    public void destroy() throws InterruptedException {
        if (channel != null) {
            channel.shutdown();
            channel.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
