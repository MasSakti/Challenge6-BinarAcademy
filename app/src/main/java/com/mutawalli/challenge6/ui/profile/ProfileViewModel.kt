package com.mutawalli.challenge6.ui.profile

import androidx.lifecycle.*
import com.mutawalli.challenge6.data.local.UserEntity
import com.mutawalli.challenge6.data.local.UserRepository
import com.mutawalli.challenge6.datastore.UserDataStoreManager
import com.mutawalli.challenge6.resource.Resource
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val pref: UserDataStoreManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _errorStatus = MutableLiveData<String?>()
    val errorStatus: LiveData<String?> get() = _errorStatus

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _userData = MutableLiveData<Resource<UserEntity>>()
    val userData: LiveData<Resource<UserEntity>> get() = _userData


    fun clearDataUser() {
        viewModelScope.launch {
            pref.logoutUser()
        }
    }

    fun userData(id: Int) {
        viewModelScope.launch {
            _userData.postValue(Resource.loading(null))
            try {
                val data = userRepository.getUser(id)
                _userData.postValue(Resource.success(data, 0))
            } catch (exception: Exception) {
                _userData.postValue(Resource.error(null, exception.message!!))
            }
        }
    }

    fun getIdUser(): LiveData<Int> {
        return pref.getId().asLiveData()
    }
}