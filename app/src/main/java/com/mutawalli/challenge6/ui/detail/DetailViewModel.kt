package com.mutawalli.challenge6.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutawalli.challenge6.data.api.ErrorMovie
import com.mutawalli.challenge6.data.api.MovieRepository
import com.mutawalli.challenge6.data.api.Movies
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository) : ViewModel() {

    private var _detail = MutableLiveData<Movies?>()
    val detail: LiveData<Movies?> get() = _detail

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private var _errorStatus = MutableLiveData<String?>()
    val errorStatus: LiveData<String?> get() = _errorStatus

    fun getDetail(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _detail.value = repository.getDetail(id)
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

}