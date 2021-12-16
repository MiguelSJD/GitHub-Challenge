package br.com.challenge.githubchallenge.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.challenge.githubchallenge.data.model.Item

@Dao
interface GitRepositoryDao {

    @Query("SELECT * FROM 'Item' WHERE language = :lang ORDER BY stargazersCount DESC")
    fun getRepositoriesByLanguage(lang:String): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

}