package br.com.challenge.githubchallenge.data.repository

import br.com.challenge.githubchallenge.data.api.ApiHelper
import br.com.challenge.githubchallenge.data.database.GitRepositoryDao
import br.com.challenge.githubchallenge.data.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainRepository(private val apiHelper: ApiHelper, private val dao:GitRepositoryDao) {

    suspend fun getRepositories(language:String, page:Int) = apiHelper.getRepositories(language, page)

    fun getRepositoriesByLang(lang:String) = dao.getRepositoriesByLanguage(lang)

    fun insert(item: Item) = runBlocking {
        launch(Dispatchers.IO){
            dao.insert(item)
        }
    }

}
