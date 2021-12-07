package br.com.challenge.githubchallenge.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.challenge.githubchallenge.data.repository.MainRepository
import br.com.challenge.githubchallenge.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class MainViewModel (private val mainRepository: MainRepository): ViewModel() {

    fun getRepositories(page:Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getRepositories(page)))
        } catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}