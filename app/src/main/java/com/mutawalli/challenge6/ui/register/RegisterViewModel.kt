package com.mutawalli.challenge6.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutawalli.challenge6.data.local.UserEntity
import com.mutawalli.challenge6.data.local.UserRepository
import com.mutawalli.challenge6.utils.Event
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _saved = MutableLiveData<Event<Boolean>>()
    val saved: LiveData<Event<Boolean>> get() = _saved

    fun save(
        email: String,
        username: String,
        fullname: String,
        ttl: String,
        address: String,
        password: String,
        image: String
    ) {
        if (email.isEmpty() || username.isEmpty() || fullname.isEmpty() || ttl.isEmpty() || address.isEmpty() || password.isEmpty()) {
            _saved.value = Event(false)
            return
        }

        val user = UserEntity(
            email = email,
            username = username,
            fullname = fullname,
            ttl = ttl,
            address = address,
            password = password,
            image = image
        )

        viewModelScope.launch {
            repository.save(user)
        }
        _saved.value = Event(true)
    }
}