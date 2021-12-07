package br.com.challenge.githubchallenge.data.api

class ApiHelper (private val apiService: ApiService) {

    companion object{
        private const val LANGUAGE = "language:kotlin"
        private const val SORT = "stars"
    }

    suspend fun getRepositories(page:Int) = apiService.getRepositories(LANGUAGE, SORT, page.toString())
}
