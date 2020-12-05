package com.dsk.trendingrepos.data.model

import com.google.gson.annotations.SerializedName

data class RepoDetails(
    val author: String,
    val avatar: String,
    val currentPeriodStars: Int,
    val description: String,
    val forks: Int,
    val language: String,
    val languageColor: String,
    val name: String,
    val stars: Int,
    val url: String,
    @SerializedName("builtBy")
    val builtBy: List<BuiltBy>
)