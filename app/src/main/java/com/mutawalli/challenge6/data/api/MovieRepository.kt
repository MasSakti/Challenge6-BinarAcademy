package com.mutawalli.challenge6.data.api

class MovieRepository(private val movieRemoteDataSource: MovieRemoteDataSource) {

    suspend fun getPopularMovie(): List<Movies>? {
        return movieRemoteDataSource.getMovies()
    }

    suspend fun getDetail(id: Int): Movies {
        return movieRemoteDataSource.getDetail(id)
    }
}