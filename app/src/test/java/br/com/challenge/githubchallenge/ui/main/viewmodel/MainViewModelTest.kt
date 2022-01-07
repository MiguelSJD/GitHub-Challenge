package br.com.challenge.githubchallenge.ui.main.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.challenge.githubchallenge.data.model.Item
import br.com.challenge.githubchallenge.data.model.Owner
import br.com.challenge.githubchallenge.data.model.Repository
import br.com.challenge.githubchallenge.data.repository.MainRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val repository = mockk<MainRepository>()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MainViewModel(repository)
        viewModel.coroutineContext = Dispatchers.Unconfined + SupervisorJob()
    }

    @Test
    fun testSuccessGetRepositories() = runBlockingTest {
        coEvery { repository.getRepositories(LANGUAGE, PAGE_ONE) } returns getRepositories()

        viewModel.getRepositories(LANGUAGE, PAGE_ONE)

        assertEquals(MainViewState.ShowRepositories(getRepositories().items.toList()), viewModel.states.value)
    }

    @Test
    fun testFailGetRepositories() = runBlockingTest {
        val exception: Exception = mockk()

        coEvery { repository.getRepositories(LANGUAGE, PAGE_ONE) } throws exception

        viewModel.getRepositories(LANGUAGE, PAGE_ONE)

        assertEquals(MainViewState.ShowOfflineRepositories(ERROR), viewModel.states.value)
    }

    private fun getRepositories() : Repository =
        Repository(listOf(getItems()))

    private fun getItems() : Item =
        Item(1,1, "itemName", LANGUAGE, getOwner(), 1, "null")

    private fun getOwner() : Owner =
        Owner("avatarUrlOwner",1, "loginOwner" )

    companion object {
        const val LANGUAGE = "Kotlin"
        const val PAGE_ONE = 1
        const val ERROR = "An Error Occurred!"
    }
}