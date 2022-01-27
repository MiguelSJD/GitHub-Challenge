package br.com.challenge.githubchallenge.data.model


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Item(
    @SerializedName("forks")
    val forks: Int,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("language")
    val language: String,
    @Embedded(prefix = "owner_")
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("updated_at")
    val updatedAt:String
):Serializable

