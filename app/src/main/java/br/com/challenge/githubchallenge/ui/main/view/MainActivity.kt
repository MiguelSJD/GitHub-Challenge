package br.com.challenge.githubchallenge.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.challenge.githubchallenge.R
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.com.challenge.githubchallenge.App
import br.com.challenge.githubchallenge.data.model.Item
import br.com.challenge.githubchallenge.ui.base.ViewModelFactory
import br.com.challenge.githubchallenge.ui.details.view.DetailsActivity
import br.com.challenge.githubchallenge.ui.main.adapter.MainAdapter
import br.com.challenge.githubchallenge.ui.main.viewmodel.MainViewModel
import br.com.challenge.githubchallenge.ui.main.viewmodel.MainViewState
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), MainAdapter.OnRepositoryClickListener {

    private val mainViewModel: MainViewModel by viewModels{
        ViewModelFactory((application as App).repository)
    }
    private lateinit var adapter: MainAdapter
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val toolbarTitle by lazy { findViewById<com.google.android.material.textview.MaterialTextView>(R.id.toolbar_title) }
    private val toolbarFilter by lazy { findViewById<ImageButton>(R.id.toolbar_filter) }

    private var isSameLanguage = true
    var language = "Kotlin"
        private set
    private var contPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.states.observe(this, { state ->
            when (state) {
                is MainViewState.ShowRepositories -> showRepositories(state.repositories)
                is MainViewState.ShowOfflineRepositories -> showOfflineRepositories()
            }
        })
        setupUI()
        getRepositories()
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbarTitle.text = getString(R.string.title_kotlin_repositories)
        toolbarFilter.visibility = View.VISIBLE
        toolbarFilter.setOnClickListener {
            val singleItems = arrayOf("Kotlin", "Java", "PHP", "C", "C++")
            val checkedItem = 0

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.label_choose_the_language))
                .setNeutralButton(resources.getString(R.string.label_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.label_confirm)) { dialog, _ ->
                    contPage = 1
                    isSameLanguage = false
                    when ((dialog as AlertDialog).listView.checkedItemPosition) {
                        0 -> {
                            language = "Kotlin"
                            getRepositories()
                            toolbarTitle.text = getString(R.string.title_kotlin_repositories)
                        }
                        1 -> {
                            language = "Java"
                            getRepositories()
                            toolbarTitle.text = getString(R.string.title_java_repositories)
                        }
                        2 -> {
                            language = "PHP"
                            getRepositories()
                            toolbarTitle.text = getString(R.string.title_php_repositories)
                        }
                        3 -> {
                            language = "C"
                            getRepositories()
                            toolbarTitle.text = getString(R.string.title_c_repositories)
                        }
                        4 -> {
                            language = "C++"
                            getRepositories()
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
                    getRepositories()
                }
            }
        })
    }

    private fun getRepositories() {
        if (isSameLanguage) contPage += 1
        progressBar?.visibility = View.VISIBLE
        mainViewModel.getRepositories(language,contPage)
    }

    private fun setupOfflineRepositories(){
        mainViewModel.getOfflineRepositories(language).observe(this,{items ->
            adapter.apply {
                addRepositories(items, !isSameLanguage)
                notifyDataSetChanged()
            }
            if (adapter.isEmpty()) Toast.makeText(this, getString(R.string.message_fail_to_recover_data_from_database), Toast.LENGTH_LONG).show()
        } )
    }

    private fun showRepositories(items: List<Item>) {
        progressBar.visibility = View.GONE
        adapter.apply {
            addRepositories(items, !isSameLanguage)
            notifyDataSetChanged()
        }
        isSameLanguage = true
        mainViewModel.saveRepositories(items)
    }

    override fun onRepositoryClicked(item: Item) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("item", item)
        startActivity(intent)
    }

    private fun showOfflineRepositories() {
        Toast.makeText(this, getString(R.string.message_fail_to_get_data_from_api), Toast.LENGTH_SHORT).show()
        isSameLanguage = false
        progressBar.visibility = View.GONE
        setupOfflineRepositories()
    }
}
