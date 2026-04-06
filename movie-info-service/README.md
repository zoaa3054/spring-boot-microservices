# Movie Info Service MongoDB Cache

## MongoDB cache schema

Collection: `cached_movies`

Fields:
- `id` (ObjectId): generated MongoDB document id
- `movieId` (String): external TMDB movie ID, unique
- `title` (String): movie title
- `description` (String): movie overview or description
- `cachedAt` (long): epoch ms when the movie was cached

Indexes:
- Unique index on `movieId`

## Cache behavior

1. Request `GET /movies/{movieId}`
2. Service checks MongoDB first using `movieId`
3. If a cached document exists, it returns the cached movie
4. If missing, it fetches from TMDB, stores the result in MongoDB, then returns the movie

## Configuration

`application.properties` loads `.env` optionally:

```properties
spring.config.import=optional:dotenv:
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/movieinfoservice}
```

## Performance test strategy

- First request for a new movie ID populates the cache and reflects remote latency
- Second request for the same ID returns from MongoDB and should be much faster
- Use a large dataset to verify the `movieId` index keeps lookup latency low
