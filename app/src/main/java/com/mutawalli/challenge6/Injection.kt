package com.mutawalli.challenge6

import android.content.Context
import com.mutawalli.challenge6.data.local.UserRepository

object Injection {

    fun provideRepositoryUser(context: Context): UserRepository? {
        return UserRepository.getInstance(context)
    }
}