package com.dsk.trendingrepos.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsk.trendingrepos.data.model.RepoDetails

@Dao
interface RepoDetailsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun upsert(objects: List<RepoDetails?>)

    @Query("SELECT * FROM repo_details")
    fun getAllRepoDetails(): LiveData<List<RepoDetails>>

}