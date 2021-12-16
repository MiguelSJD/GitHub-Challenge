package br.com.challenge.githubchallenge.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.challenge.githubchallenge.data.model.Item
import br.com.challenge.githubchallenge.data.model.Owner

@Database(entities = [Item::class], version = 1)
abstract class AppDataBase: RoomDatabase() {

    abstract fun repositoryDao() : GitRepositoryDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getDataBase(context: Context): AppDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "gitrepository_db"
                ).build()
                INSTANCE= instance
                return instance
            }
        }
    }
}