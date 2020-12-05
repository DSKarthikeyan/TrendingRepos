package com.dsk.trendingrepos.data.api

import com.dsk.trendingrepos.data.model.RepoDetails
import retrofit2.Response
import retrofit2.http.GET

interface TrendingRepoApi {

    @GET("repositories")
    suspend fun getTrendingRepo(): Response<List<RepoDetails>>

}