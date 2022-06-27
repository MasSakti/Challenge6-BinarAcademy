package com.mutawalli.challenge6.ui.home

import androidx.lifecycle.*
import com.mutawalli.challenge6.data.api.ErrorMovie
import com.mutawalli.challenge6.data.api.MovieRepository
import com.mutawalli.challenge6.data.api.Movies
import com.mutawalli.challenge6.data.local.UserEntity
import com.mutawalli.challenge6.data.local.UserRepository
import com.mutawalli.challenge6.datastore.UserDataStoreManager
import com.mutawalli.challenge6.resource.Resource
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository,
    private val userRepository: UserRepository,
    private val pref: UserDataStoreManager
) : ViewModel() {

    private var _popular = MutableLiveData<List<Movies>>()
    val popular: LiveData<List<Movies>> get() = _popular

    private var _errorStatus = MutableLiveData<String?>()
    val errorStatus: LiveData<String?> get() = _errorStatus

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _userData = MutableLiveData<Resource<UserEntity>>()
    val userData: LiveData<Resource<UserEntity>> get() = _userData

    init {
        getPopularMovie()
    }


    fun getPopularMovie() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _popular.value = repository.getPopularMovie()
            } catch (error: ErrorMovie) {
                _errorStatus.value = error.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun onSnackbarShown() {
        _errorStatus.value = null
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