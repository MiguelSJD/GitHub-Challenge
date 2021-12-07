package br.com.challenge.githubchallenge.ui.main.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.challenge.githubchallenge.R
import br.com.challenge.githubchallenge.data.api.ApiHelper
import br.com.challenge.githubchallenge.data.api.RetrofitBuilder
import br.com.challenge.githubchallenge.data.model.Item
import br.com.challenge.githubchallenge.ui.base.ViewModelFactory
import br.com.challenge.githubchallenge.ui.main.adapter.MainAdapter
import br.com.challenge.githubchallenge.ui.main.viewmodel.MainViewModel
import br.com.challenge.githubchallenge.utils.Status.SUCCESS
import br.com.challenge.githubchallenge.utils.Status.ERROR
import br.com.challenge.githubchallenge.utils.Status.LOADING

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }

    private var contPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    setupObservers()
                }
            }
        })
    }

    private fun setupObservers() {
        contPage += 1
        viewModel.getRepositories(contPage).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { item -> retrieveList(item.items) }
                    }
                    ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun retrieveList(item: List<Item>) {
        adapter.apply {
            addRepositories(item)
            notifyDataSetChanged()
        }
    }
}