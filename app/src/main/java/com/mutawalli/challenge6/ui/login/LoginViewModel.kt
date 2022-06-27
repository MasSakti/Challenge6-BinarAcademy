package com.mutawalli.challenge6.ui.login

import androidx.lifecycle.*
import com.mutawalli.challenge6.data.local.UserEntity
import com.mutawalli.challenge6.data.local.UserRepository
import com.mutawalli.challenge6.datastore.UserDataStoreManager
import com.mutawalli.challenge6.resource.Resource
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository,
    private val pref: UserDataStoreManager
) : ViewModel() {

    private var _loginStatus = MutableLiveData<Resource<UserEntity>>()
    val loginStatus: LiveData<Resource<UserEntity>> get() = _loginStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.postValue(Resource.loading(null))
            try {
                val data = repository.verifyLogin(email, password)
                _loginStatus.postValue(Resource.success(data, 0))
            } catch (exception: Exception) {
                _loginStatus.postValue(Resource.error(null, exception.message!!))
            }
        }
    }

    fun saveUserDataStore(status: Boolean, id: Int) {
        viewModelScope.launch {
            pref.saveUser(status, id)
        }
    }

    fun getStatus(): LiveData<Boolean> {
        return pref.getStatus().asLiveData()
    }

}