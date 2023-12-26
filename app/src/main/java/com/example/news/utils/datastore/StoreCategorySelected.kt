package com.example.news.utils.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.news.utils.Categories
import com.example.news.utils.Const
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreCategorySelected(private val context: Context) {
    // to make sure there's only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Const.CATEGORY_SELECTED)
        val CATEGORY_SELECTED_KEY = stringPreferencesKey(Const.CATEGORY_SELECTED)
    }

    val getCategorySelected: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[CATEGORY_SELECTED_KEY] ?: Categories.GENERAL.name
        }

    suspend fun saveCategorySelected(value: String) {
        context.dataStore.edit { preferences ->
            preferences[CATEGORY_SELECTED_KEY] = value
        }
    }
}