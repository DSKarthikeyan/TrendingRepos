package com.dsk.trendingrepos.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dsk.trendingrepos.data.model.RepoDetails

@Database(
    entities = [RepoDetails::class],
    version = 1, exportSchema = false
)
abstract class TrendingRepoDatabase : RoomDatabase() {

    abstract fun getRepoDetailsDao(): RepoDetailsDAO

    companion object {
        @Volatile
        private var instance: TrendingRepoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                TrendingRepoDatabase::class.java,
                "trending_repo.db"
            ).build()
    }


}