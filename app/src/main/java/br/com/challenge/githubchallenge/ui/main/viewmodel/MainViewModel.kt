package br.com.challenge.githubchallenge.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.challenge.githubchallenge.data.model.Item
import br.com.challenge.githubchallenge.data.repository.MainRepository
import br.com.challenge.githubchallenge.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class MainViewModel (private val mainRepository: MainRepository): ViewModel() {

    fun getOfflineRepositories(language: String): LiveData<List<Item>>{
        return mainRepository.getRepositoriesByLang(language)
    }

    fun saveRepositories(items:List<Item>){
        items.forEach{ item -> mainRepository.insert(item) }
    }

    fun getRepositories(language:String, page:Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getRepositories(language, page)))
        } catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}