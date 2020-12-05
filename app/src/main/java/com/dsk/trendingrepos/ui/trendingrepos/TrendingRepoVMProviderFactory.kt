package com.dsk.trendingrepos.ui.trendingrepos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsk.trendingrepos.data.repository.TrendingRepoRepository

class TrendingRepoVMProviderFactory(private val trendingRepoRepository: TrendingRepoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrendingRepoViewModel(trendingRepoRepository) as T
    }
}