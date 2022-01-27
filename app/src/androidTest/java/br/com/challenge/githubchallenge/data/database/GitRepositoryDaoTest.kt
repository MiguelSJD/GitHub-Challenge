package br.com.challenge.githubchallenge.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import br.com.challenge.githubchallenge.data.model.Item
import br.com.challenge.githubchallenge.data.model.Owner
import br.com.challenge.githubchallenge.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class GitRepositoryDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database:AppDataBase
    private lateinit var dao:GitRepositoryDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),AppDataBase::class.java).allowMainThreadQueries().build()
        dao = database.repositoryDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    private val owner = Owner("avatarurl",1,"owner1")
    private val repository = Item(1,1,"example1","Kotlin", owner, 1, "null")

    @Test
    fun insertRepository(){
        dao.insert(repository)

        val allRepositories = dao.getRepositoriesByLanguage("Kotlin").getOrAwaitValue()

        assertThat(allRepositories).contains(Item(1,1,"example1","Kotlin", Owner("avatarurl",1,"owner1"), 1, "null"))
    }

    @Test
    fun getAllRepositories() {
        dao.insert(repository)
        dao.insert(Item(2, 2,"Example2", "Kotlin", owner , 2, "null"))

        val allRepositories = dao.getRepositoriesByLanguage("Kotlin").getOrAwaitValue()

        assertThat(allRepositories).contains(Item(1,1,"example1","Kotlin", Owner("avatarurl",1,"owner1"), 1, "null"))
        assertThat(allRepositories).contains(Item(2, 2,"Example2", "Kotlin", owner , 2, "null"))
    }

}