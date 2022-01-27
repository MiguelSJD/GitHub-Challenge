package br.com.challenge.githubchallenge.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.challenge.githubchallenge.data.model.Item
import br.com.challenge.githubchallenge.data.repository.MainRepository
import br.com.challenge.githubchallenge.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val mainRepository: MainRepository) : BaseViewModel() {
    private val _states = MutableLiveData<MainViewState>()
    val states: LiveData<MainViewState>
        get() = _states

    fun getRepositories(language: String, page: Int) {
        launch {
            try {
                val response = mainRepository.getRepositories(language, page)
                _states.value = MainViewState.ShowRepositories(response.items.toList())
            } catch (exception: Exception) {
                _states.value = MainViewState.ShowOfflineRepositories("An Error Occurred!")
            }
        }
    }

    fun getOfflineRepositories(language: String): LiveData<List<Item>> {
        return mainRepository.getRepositoriesByLang(language)
    }

    fun saveRepositories(items: List<Item>) {
        items.forEach { item -> mainRepository.insert(item) }
    }
}

sealed class MainViewState {
    data class ShowRepositories(val repositories: List<Item>) : MainViewState()
    data class ShowOfflineRepositories(val error: String) : MainViewState()
}