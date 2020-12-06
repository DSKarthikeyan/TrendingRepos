package com.dsk.trendingrepos.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dsk.trendingrepos.data.db.Converters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repo_details",indices = [Index(value = ["author","name"], unique = true)])
data class RepoDetails(
    @SerializedName("author")
    val author: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("currentPeriodStars")
    val currentPeriodStars: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("forks")
    val forks: Int,
    @SerializedName("language")
    val language: String,
    @SerializedName("languageColor")
    val languageColor: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("builtBy")
    @field:TypeConverters(Converters::class)
    val builtBy: List<BuiltBy>
){
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int? = null
}
