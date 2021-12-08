package br.com.challenge.githubchallenge.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String,
):Serializable
