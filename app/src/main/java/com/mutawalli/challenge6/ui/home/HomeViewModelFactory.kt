package com.mutawalli.challenge6.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mutawalli.challenge6.Injection
import com.mutawalli.challenge6.data.api.MovieRepository
import com.mutawalli.challenge6.data.local.UserRepository
import com.mutawalli.challenge6.datastore.UserDataStoreManager

class HomeViewModelFactory(
    private val repository: MovieRepository,
    private val userRepository: UserRepository?,
    private val pref: UserDataStoreManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository, userRepository!!, pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(
            context: Context,
            repository: MovieRepository,
            pref: UserDataStoreManager
        ): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(
                    repository,
                    Injection.provideRepositoryUser(context),
                    pref
                )
            }.also { instance = it }
    }
}