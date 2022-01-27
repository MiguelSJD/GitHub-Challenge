package br.com.challenge.githubchallenge.data.api

import br.com.challenge.githubchallenge.data.model.Repository
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/repositories")
    suspend fun getRepositories(
        @Query("q") q:String,
        @Query("sort") sort:String,
        @Query("page") page:String
    ): Repository
}
