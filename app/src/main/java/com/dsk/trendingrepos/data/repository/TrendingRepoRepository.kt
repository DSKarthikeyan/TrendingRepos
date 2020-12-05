package com.dsk.trendingrepos.data.repository

import com.dsk.trendingrepos.data.api.RetrofitInstance

class TrendingRepoRepository() {

    suspend fun getTrendingRepo() = RetrofitInstance.api.getTrendingRepo()
}