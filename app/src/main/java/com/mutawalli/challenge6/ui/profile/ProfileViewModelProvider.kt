package com.mutawalli.challenge6.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mutawalli.challenge6.Injection
import com.mutawalli.challenge6.data.local.UserRepository
import com.mutawalli.challenge6.datastore.UserDataStoreManager

class ProfileViewModelProvider(
    private val pref: UserDataStoreManager,
    private val userRepository: UserRepository?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(pref, userRepository!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: ProfileViewModelProvider? = null
        fun getInstance(
            context: Context,
            pref: UserDataStoreManager
        ): ProfileViewModelProvider =
            instance ?: synchronized(this) {
                instance ?: ProfileViewModelProvider(
                    pref,
                    Injection.provideRepositoryUser(context)
                )
            }.also { instance = it }
    }
}