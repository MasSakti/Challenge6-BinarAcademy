package com.mutawalli.challenge6.data.api

import com.mutawalli.challenge6.network.MovieApiClient

class MovieRemoteDataSource {
    suspend fun getMovies(): List<Movies>? {
        try {
            return MovieApiClient.instance.getPopular().results
        }catch (cause: Throwable){
            throw ErrorMovie("Data Gagal Diload", cause)
        }

    }

    suspend fun getDetail(id: Int): Movies {
        try {
            return MovieApiClient.instance.getDetail(id).body()!!
        } catch (cause: Throwable){
            throw ErrorMovie("Ada kesalahan saat load detail", cause)
        }
    }
}

class ErrorMovie(message: String, cause: Throwable?) : Throwable(message, cause)