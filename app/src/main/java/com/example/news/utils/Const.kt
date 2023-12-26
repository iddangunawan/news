package com.example.news.utils

object Const {
    const val BASE_URL_NEWS = "https://newsapi.org/v2/"
    const val COUNTRY_ID = "us"
    const val PAGE_SIZE = 10
    const val DEFAULT_IMAGE =
        "https://cdn.digitbin.com/wp-content/uploads/how-to-change-default-app-icons-on-mac.jpeg"

    // Datastore Key
    const val CATEGORY_SELECTED = "categorySelected"

    // Module - Provides - Named
    object Provides {
        const val BASE_URL_NEWS = "BASE_URL_NEWS"
        const val API_KEY_NEWS = "API_KEY_NEWS"
        const val COUNTRY_NEWS = "COUNTRY_NEWS"
        const val PAGE_SIZE = "PAGE_SIZE"
    }
}