package com.dsk.trendingrepos.data.repository

import com.dsk.trendingrepos.data.api.RetrofitInstance
import com.dsk.trendingrepos.data.db.TrendingRepoDatabase
import com.dsk.trendingrepos.data.model.RepoDetails

class TrendingRepoRepository(private val trendingRepoDatabase: TrendingRepoDatabase) {

    suspend fun getTrendingRepo() = RetrofitInstance.api.getTrendingRepo()

    suspend fun upsert(reposDetails: List<RepoDetails>) =
        trendingRepoDatabase.getRepoDetailsDao().upsert(reposDetails)

    fun getRepoDetailsFromLocal() = trendingRepoDatabase.getRepoDetailsDao().getAllRepoDetails()
}