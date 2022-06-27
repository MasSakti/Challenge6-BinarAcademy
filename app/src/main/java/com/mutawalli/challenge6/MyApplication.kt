package com.mutawalli.challenge6

import android.app.Application
import com.mutawalli.challenge6.data.api.MovieRemoteDataSource
import com.mutawalli.challenge6.data.api.MovieRepository

class MyApplication: Application() {

    private val movieRemoteDataSource by lazy { MovieRemoteDataSource() }
    val repository by lazy { MovieRepository(movieRemoteDataSource) }
}