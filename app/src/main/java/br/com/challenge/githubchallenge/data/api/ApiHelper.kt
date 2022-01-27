package br.com.challenge.githubchallenge.data.api

class ApiHelper (private val apiService: ApiService) {

    companion object{
        private const val SORT = "stars"
    }

    suspend fun getRepositories(language:String, page:Int) = apiService.getRepositories(("language:$language"), SORT, page.toString())
}
