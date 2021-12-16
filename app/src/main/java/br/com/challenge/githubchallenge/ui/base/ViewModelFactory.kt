package br.com.challenge.githubchallenge.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.challenge.githubchallenge.data.repository.MainRepository
import br.com.challenge.githubchallenge.ui.main.viewmodel.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val mainRepository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}