package com.dsk.trendingrepos.ui.trendingrepos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsk.trendingrepos.TrendingRepoApplication
import com.dsk.trendingrepos.data.repository.TrendingRepoRepository

class TrendingRepoVMProviderFactory(
    private val application: TrendingRepoApplication,
    private val trendingRepoRepository: TrendingRepoRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrendingRepoViewModel(application,trendingRepoRepository) as T
    }
}