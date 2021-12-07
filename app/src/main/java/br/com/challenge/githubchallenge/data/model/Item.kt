package br.com.challenge.githubchallenge.data.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("forks")
    val forks: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("updated_at")
    val updatedAt:String
)
