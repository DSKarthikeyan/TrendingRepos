package com.dsk.trendingrepos.data.repository

import com.dsk.trendingrepos.data.api.RetrofitInstance
import com.dsk.trendingrepos.data.db.TrendingRepoDatabase
import com.dsk.trendingrepos.data.model.RepoDetails

class TrendingRepoRepository(private val trendingRepoDatabase: TrendingRepoDatabase) {

    /**
     * fun: toGet Trending Repos from server using Retrofit
     */
    suspend fun getTrendingRepo() = RetrofitInstance.api.getTrendingRepo()

    /**
     * fun: To Insert List of Repo details to Local DB
     */
    suspend fun upsert(reposDetails: List<RepoDetails>) =
        trendingRepoDatabase.getRepoDetailsDao().upsert(reposDetails)

    /**
     * fun: toGet All Live Trending Repo Details from Local DB
     */
    fun getRepoDetailsFromLocal() = trendingRepoDatabase.getRepoDetailsDao().getAllRepoDetails()
}