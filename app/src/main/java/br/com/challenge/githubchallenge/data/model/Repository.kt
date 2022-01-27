package br.com.challenge.githubchallenge.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Repository(
    @SerializedName("items")
    val items: List<Item>,
):Serializable
