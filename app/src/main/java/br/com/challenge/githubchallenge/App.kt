package br.com.challenge.githubchallenge

import android.app.Application
import br.com.challenge.githubchallenge.data.api.ApiHelper
import br.com.challenge.githubchallenge.data.api.RetrofitBuilder
import br.com.challenge.githubchallenge.data.database.AppDataBase
import br.com.challenge.githubchallenge.data.repository.MainRepository

class App:Application() {
    private val apiHelper by lazy { ApiHelper(RetrofitBuilder.apiService) }
    private val database by lazy { AppDataBase.getDataBase(this) }
    val repository by lazy { MainRepository(dao = database.repositoryDao(), apiHelper = apiHelper) }
}