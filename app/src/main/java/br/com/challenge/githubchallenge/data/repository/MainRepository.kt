package br.com.challenge.githubchallenge.data.repository

import br.com.challenge.githubchallenge.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getRepositories(page:Int) = apiHelper.getRepositories(page)

}
