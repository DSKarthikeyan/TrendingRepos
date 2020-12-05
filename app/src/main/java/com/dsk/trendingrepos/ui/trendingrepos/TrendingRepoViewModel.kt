package com.dsk.trendingrepos.ui.trendingrepos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsk.trendingrepos.data.model.RepoDetails
import com.dsk.trendingrepos.data.repository.TrendingRepoRepository
import com.dsk.trendingrepos.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class TrendingRepoViewModel(private val trendingRepoRepository: TrendingRepoRepository) : ViewModel() {

    val trendingRepoList: MutableLiveData<Resource<List<RepoDetails>>> = MutableLiveData()

    init {
        getTrendingRepo()
    }

    private fun getTrendingRepo() = viewModelScope.launch {
        trendingRepoList.postValue(Resource.Loading())
        val response = trendingRepoRepository.getTrendingRepo()
        trendingRepoList.postValue(handleTrendingReposResponse(response))
    }

    private fun handleTrendingReposResponse(response: Response<List<RepoDetails>>) : Resource<List<RepoDetails>>? {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())

    }
}