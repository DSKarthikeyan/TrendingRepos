package com.dsk.trendingrepos.ui.trendingrepos

import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsk.trendingrepos.R
import com.dsk.trendingrepos.adapter.TrendingRepoAdapter
import com.dsk.trendingrepos.data.repository.TrendingRepoRepository
import com.dsk.trendingrepos.util.Resource


class TrendingRepoActivity : AppCompatActivity() {

    lateinit var trendingRepoViewModel: TrendingRepoViewModel
    lateinit var trendingRepoAdapter: TrendingRepoAdapter
    lateinit var trendingRepoRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trending_repo)

        initTrendingRepoView()
    }

    private fun initTrendingRepoView(){
        trendingRepoRecyclerView = findViewById(R.id.recyclerViewRepoList)
        val trendingRepoRepository= TrendingRepoRepository()
        val viewModelProviderFactory = TrendingRepoVMProviderFactory(trendingRepoRepository)

        trendingRepoViewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            TrendingRepoViewModel::class.java
        )
        trendingRepoViewModel.trendingRepoList.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        trendingRepoAdapter.differ.submitList(newsResponse)
                        Log.d("DSK", " $newsResponse");
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("DSK ", "An error occured: $message")
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
//        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
//        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        trendingRepoAdapter = TrendingRepoAdapter()
        trendingRepoRecyclerView.apply {
            adapter = trendingRepoAdapter
            layoutManager = LinearLayoutManager(this@TrendingRepoActivity)
           setDivider(R.drawable.solid_line)
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
}