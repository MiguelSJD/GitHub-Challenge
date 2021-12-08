package br.com.challenge.githubchallenge.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import br.com.challenge.githubchallenge.ui.details.view.DetailsActivity
import br.com.challenge.githubchallenge.ui.main.adapter.MainAdapter
import br.com.challenge.githubchallenge.ui.main.viewmodel.MainViewModel
import br.com.challenge.githubchallenge.utils.Status.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), MainAdapter.OnRepositoryClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val toolbarTitle by lazy { findViewById<com.google.android.material.textview.MaterialTextView>(R.id.toolbar_title) }
    private val toolbarFilter by lazy { findViewById<ImageButton>(R.id.toolbar_filter) }

    var language = "language:kotlin"
        private set
    private var contPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
        setupObservers()
        setupToolbar()
    }

    private fun setupToolbar(){
        toolbarTitle.text = getString(R.string.title_kotlin_repositories)
        toolbarFilter.visibility = View.VISIBLE
        toolbarFilter.setOnClickListener {
            val singleItems = arrayOf("Kotlin", "Java", "PHP" ,"C", "C++")
            val checkedItem = 0

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.label_choose_the_language))
                .setNeutralButton(resources.getString(R.string.label_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.label_confirm)) { dialog, _ ->
                    when((dialog as AlertDialog).listView.checkedItemPosition){
                        0 -> {
                            language = "language:kotlin"
                            updateObservers()
                            toolbarTitle.text = getString(R.string.title_kotlin_repositories)
                        }
                        1 -> {
                            language = "language:java"
                            updateObservers()
                            toolbarTitle.text = getString(R.string.title_java_repositories)
                        }
                        2 -> {
                            language = "language:php"
                            updateObservers()
                            toolbarTitle.text = getString(R.string.title_php_repositories)
                        }
                        3 -> {
                            language = "language:c"
                            updateObservers()
                            toolbarTitle.text = getString(R.string.title_c_repositories)
                        }
                        4 -> {
                            language = "language:c++"
                            updateObservers()
                            toolbarTitle.text = getString(R.string.title_cplusplus_repositories)
                        }
                    }
                }
                .setSingleChoiceItems(singleItems, checkedItem) { _, _ ->
                    // Respond to item chosen
                }
                .show()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf(), this)
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
        viewModel.getRepositories(language, contPage).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { item -> getList(item.items) }
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

    private fun updateObservers() {
        contPage = 1
        viewModel.getRepositories(language, contPage).observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { item -> updateList(item.items) }
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

    private fun getList(item: List<Item>) {
        adapter.apply {
            addRepositories(item)
            notifyDataSetChanged()
        }
    }

    private fun updateList(item: List<Item>) {
        adapter.apply {
            updateRepositories(item)
            notifyDataSetChanged()
        }
    }

    override fun onRepositoryClicked(item: Item) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }

}