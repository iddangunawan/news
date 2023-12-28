package com.example.news.domain.model

import com.google.gson.annotations.SerializedName

data class TopHeadlinesSourcesResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("sources")
    val sources: List<Sources> = listOf(),
)

data class Sources(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("url")
    val url: String,
    @field:SerializedName("category")
    val category: String,
    @field:SerializedName("language")
    val language: String,
    @field:SerializedName("country")
    val country: String,
)