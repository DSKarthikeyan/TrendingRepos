package com.dsk.trendingrepos.ui.trendingrepos

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dsk.trendingrepos.R
import com.dsk.trendingrepos.TrendingRepoApplication
import com.dsk.trendingrepos.adapter.TrendingRepoAdapter
import com.dsk.trendingrepos.data.db.TrendingRepoDatabase
import com.dsk.trendingrepos.data.model.RepoDetails
import com.dsk.trendingrepos.data.repository.TrendingRepoRepository
import com.dsk.trendingrepos.util.Resource


class TrendingRepoActivity : AppCompatActivity() {

    lateinit var trendingRepoViewModel: TrendingRepoViewModel
    lateinit var trendingRepoAdapter: TrendingRepoAdapter
    lateinit var trendingRepoRecyclerView: RecyclerView
    lateinit var swipeToRefreshRepoList: SwipeRefreshLayout
    lateinit var progressBarCircular: ProgressBar
    private lateinit var textViewStatus: TextView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trending_repo)

        initializeViews()
        initTrendingRepoView()
    }

    private fun initializeViews() {
        trendingRepoRecyclerView = findViewById(R.id.recyclerViewRepoList)
        swipeToRefreshRepoList = findViewById(R.id.swipeRefreshRepoList)
        progressBarCircular = findViewById(R.id.progressCircularBar)
        textViewStatus = findViewById(R.id.textViewStatus)

    }

    private fun initTrendingRepoView() {
        val trendingRepoDatabase = TrendingRepoDatabase(this)
        val trendingRepoRepository = TrendingRepoRepository(trendingRepoDatabase)
        val viewModelProviderFactory = TrendingRepoVMProviderFactory(
            application as TrendingRepoApplication,
            trendingRepoRepository
        )

        trendingRepoViewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            TrendingRepoViewModel::class.java
        )

        trendingRepoViewModel.getTrendingRepoFromLocal().observe(this, { response ->
            if (response.isNotEmpty()) {
                trendingRepoAdapter.differ.submitList(response)
            } else {
                trendingRepoViewModel.getTrendingRepo()
            }
        })

        trendingRepoViewModel.trendingRepoList.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        trendingRepoAdapter.differ.submitList(newsResponse)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        showLoadingText(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        setupRecyclerView()

    }

    private fun hideProgressBar() {
        trendingRepoRecyclerView.visibility = View.VISIBLE
        progressBarCircular.visibility = View.INVISIBLE
        textViewStatus.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        trendingRepoRecyclerView.visibility = View.INVISIBLE
        progressBarCircular.visibility = View.VISIBLE
        showLoadingText(resources.getString(R.string.text_loading))
    }

    private fun showLoadingText(message: String) {
        if (message.isNotEmpty()) {
            trendingRepoRecyclerView.visibility = View.INVISIBLE
            textViewStatus.visibility = View.VISIBLE
            textViewStatus.text = message
        }
    }

    private fun setupRecyclerView() {
        trendingRepoAdapter = TrendingRepoAdapter()
        trendingRepoRecyclerView.apply {
            adapter = trendingRepoAdapter
            layoutManager = LinearLayoutManager(this@TrendingRepoActivity)
            setDivider(R.drawable.solid_line)
        }

        setupSwipeToRefresh()
    }

    private fun setupSwipeToRefresh() {
        swipeToRefreshRepoList.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.teal_200))
        swipeToRefreshRepoList.setColorSchemeColors(Color.WHITE)

        swipeToRefreshRepoList.setOnRefreshListener {
            trendingRepoViewModel.getTrendingRepo()
            swipeToRefreshRepoList.isRefreshing = false
        }
    }

    private fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
        val divider = DividerItemDecoration(
            this.context,
            DividerItemDecoration.VERTICAL
        )
        val drawable = ContextCompat.getDrawable(
            this.context,
            drawableRes
        )
        drawable?.let {
            divider.setDrawable(it)
            addItemDecoration(divider)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_bar, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnCloseListener {
           Log.d("DSK 111","Close 11")
            false
        }
        searchManager.setOnDismissListener {
            // return the activity to the normal state
            Log.d("DSK 111","dismiss 11")
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                trendingRepoAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                trendingRepoAdapter.filter.filter(query)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }
}