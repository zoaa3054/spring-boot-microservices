package com.trendingmoviesservice.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: trending.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class TrendingServiceGrpc {

  private TrendingServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "trending.TrendingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.trendingmoviesservice.grpc.TopTrendingMoviesRequest,
      com.trendingmoviesservice.grpc.TopTrendingMoviesResponse> getGetTopTrendingMoviesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTopTrendingMovies",
      requestType = com.trendingmoviesservice.grpc.TopTrendingMoviesRequest.class,
      responseType = com.trendingmoviesservice.grpc.TopTrendingMoviesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.trendingmoviesservice.grpc.TopTrendingMoviesRequest,
      com.trendingmoviesservice.grpc.TopTrendingMoviesResponse> getGetTopTrendingMoviesMethod() {
    io.grpc.MethodDescriptor<com.trendingmoviesservice.grpc.TopTrendingMoviesRequest, com.trendingmoviesservice.grpc.TopTrendingMoviesResponse> getGetTopTrendingMoviesMethod;
    if ((getGetTopTrendingMoviesMethod = TrendingServiceGrpc.getGetTopTrendingMoviesMethod) == null) {
      synchronized (TrendingServiceGrpc.class) {
        if ((getGetTopTrendingMoviesMethod = TrendingServiceGrpc.getGetTopTrendingMoviesMethod) == null) {
          TrendingServiceGrpc.getGetTopTrendingMoviesMethod = getGetTopTrendingMoviesMethod =
              io.grpc.MethodDescriptor.<com.trendingmoviesservice.grpc.TopTrendingMoviesRequest, com.trendingmoviesservice.grpc.TopTrendingMoviesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTopTrendingMovies"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.trendingmoviesservice.grpc.TopTrendingMoviesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.trendingmoviesservice.grpc.TopTrendingMoviesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TrendingServiceMethodDescriptorSupplier("GetTopTrendingMovies"))
              .build();
        }
      }
    }
    return getGetTopTrendingMoviesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TrendingServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TrendingServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TrendingServiceStub>() {
        @java.lang.Override
        public TrendingServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TrendingServiceStub(channel, callOptions);
        }
      };
    return TrendingServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TrendingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TrendingServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TrendingServiceBlockingStub>() {
        @java.lang.Override
        public TrendingServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TrendingServiceBlockingStub(channel, callOptions);
        }
      };
    return TrendingServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TrendingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TrendingServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TrendingServiceFutureStub>() {
        @java.lang.Override
        public TrendingServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TrendingServiceFutureStub(channel, callOptions);
        }
      };
    return TrendingServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getTopTrendingMovies(com.trendingmoviesservice.grpc.TopTrendingMoviesRequest request,
        io.grpc.stub.StreamObserver<com.trendingmoviesservice.grpc.TopTrendingMoviesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTopTrendingMoviesMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TrendingService.
   */
  public static abstract class TrendingServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TrendingServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TrendingService.
   */
  public static final class TrendingServiceStub
      extends io.grpc.stub.AbstractAsyncStub<TrendingServiceStub> {
    private TrendingServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TrendingServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TrendingServiceStub(channel, callOptions);
    }

    /**
     */
    public void getTopTrendingMovies(com.trendingmoviesservice.grpc.TopTrendingMoviesRequest request,
        io.grpc.stub.StreamObserver<com.trendingmoviesservice.grpc.TopTrendingMoviesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTopTrendingMoviesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TrendingService.
   */
  public static final class TrendingServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TrendingServiceBlockingStub> {
    private TrendingServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TrendingServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TrendingServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.trendingmoviesservice.grpc.TopTrendingMoviesResponse getTopTrendingMovies(com.trendingmoviesservice.grpc.TopTrendingMoviesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTopTrendingMoviesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TrendingService.
   */
  public static final class TrendingServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<TrendingServiceFutureStub> {
    private TrendingServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TrendingServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TrendingServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.trendingmoviesservice.grpc.TopTrendingMoviesResponse> getTopTrendingMovies(
        com.trendingmoviesservice.grpc.TopTrendingMoviesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTopTrendingMoviesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_TOP_TRENDING_MOVIES = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_TOP_TRENDING_MOVIES:
          serviceImpl.getTopTrendingMovies((com.trendingmoviesservice.grpc.TopTrendingMoviesRequest) request,
              (io.grpc.stub.StreamObserver<com.trendingmoviesservice.grpc.TopTrendingMoviesResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetTopTrendingMoviesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.trendingmoviesservice.grpc.TopTrendingMoviesRequest,
              com.trendingmoviesservice.grpc.TopTrendingMoviesResponse>(
                service, METHODID_GET_TOP_TRENDING_MOVIES)))
        .build();
  }

  private static abstract class TrendingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TrendingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.trendingmoviesservice.grpc.TrendingProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TrendingService");
    }
  }

  private static final class TrendingServiceFileDescriptorSupplier
      extends TrendingServiceBaseDescriptorSupplier {
    TrendingServiceFileDescriptorSupplier() {}
  }

  private static final class TrendingServiceMethodDescriptorSupplier
      extends TrendingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TrendingServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TrendingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TrendingServiceFileDescriptorSupplier())
              .addMethod(getGetTopTrendingMoviesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
