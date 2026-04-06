package com.trendingmoviesservice.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class GrpcServerLifecycle implements InitializingBean, DisposableBean {

    private final int grpcPort;
    private final TrendingGrpcService trendingGrpcService;

    private Server server;

    public GrpcServerLifecycle(@Value("${grpc.server.port:9090}") int grpcPort,
                               TrendingGrpcService trendingGrpcService) {
        this.grpcPort = grpcPort;
        this.trendingGrpcService = trendingGrpcService;
    }

    @Override
    public void afterPropertiesSet() throws IOException {
        server = ServerBuilder.forPort(grpcPort)
                .addService(trendingGrpcService)
                .build()
                .start();
    }

    @Override
    public void destroy() throws InterruptedException {
        if (server != null) {
            server.shutdown();
            server.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
