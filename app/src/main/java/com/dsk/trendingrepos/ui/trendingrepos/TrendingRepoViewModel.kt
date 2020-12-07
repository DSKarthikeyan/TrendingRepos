package com.dsk.trendingrepos.ui.trendingrepos

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.dsk.trendingrepos.TrendingRepoApplication
import com.dsk.trendingrepos.data.model.RepoDetails
import com.dsk.trendingrepos.data.repository.TrendingRepoRepository
import com.dsk.trendingrepos.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class TrendingRepoViewModel(
    application: TrendingRepoApplication,
    private val trendingRepoRepository: TrendingRepoRepository
) : AndroidViewModel(application) {

    val trendingRepoList: MutableLiveData<Resource<List<RepoDetails>>> = MutableLiveData()

    /**
     * fun: to get Trending Repo details from server
     */
    fun getTrendingRepo() = viewModelScope.launch {
        getTrendingRepoDetails()
    }

    /**
     * fun: get trending repo from local database through repository
     */
    fun getTrendingRepoFromLocal() = trendingRepoRepository.getRepoDetailsFromLocal()

    /**
     * fun: get trending repo from server
     */
    private suspend fun getTrendingRepoDetails() {
        try {
            if (hasInternetConnection()) {
                trendingRepoList.postValue(Resource.Loading())
                val response = trendingRepoRepository.getTrendingRepo()
                trendingRepoList.postValue(handleTrendingReposResponse(response))
            } else {
                trendingRepoList.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> trendingRepoList.postValue(Resource.Error("No Internet Connection"))
                else -> trendingRepoList.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    /**
     * fun: toHandle RepoDetails input and insert to LocalDB
     *    and to return if Server fetched response is successful
     */
    private fun handleTrendingReposResponse(response: Response<List<RepoDetails>>): Resource<List<RepoDetails>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                viewModelScope.launch(Dispatchers.IO) {
                    trendingRepoRepository.upsert(resultResponse)
                }
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * fun: to handle Internet Connectivity
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<TrendingRepoApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}