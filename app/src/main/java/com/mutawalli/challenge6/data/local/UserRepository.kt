package com.mutawalli.challenge6.data.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDAO) {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(context: Context): UserRepository? {
            return instance ?: synchronized(UserRepository::class.java) {
                if (instance == null) {
                    val database = UserDatabase.getInstance(context)
                    instance = UserRepository(database.userDao())
                }
                return instance
            }
        }
    }

    suspend fun save(user: UserEntity) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    suspend fun update(user: UserEntity) {
        withContext(Dispatchers.IO) {
            userDao.update(user)
        }
    }

    suspend fun getUser(id: Int): UserEntity {
        return userDao.getUser(id)
    }

    suspend fun verifyLogin(email: String, password: String): UserEntity {
        return userDao.readLogin(email, password)
    }
}