package com.example.collegeschedule.data.repository

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesRepository(context: Context) {
    private val sharedPref = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val FAVORITES_KEY = "favorite_groups"

    private val _favoriteGroups = MutableStateFlow(getFavorites())
    val favoriteGroups: StateFlow<Set<String>> = _favoriteGroups.asStateFlow()

    private fun getFavorites(): Set<String> {
        return sharedPref.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }

    fun toggleFavorite(groupName: String) {
        val current = getFavorites().toMutableSet()
        if (groupName in current) {
            current.remove(groupName)
        } else {
            current.add(groupName)
        }

        sharedPref.edit {
            putStringSet(FAVORITES_KEY, current)
        }
        _favoriteGroups.value = current
    }

    fun isFavorite(groupName: String): Boolean {
        return groupName in getFavorites()
    }

    fun getFavoritesList(): List<String> {
        return getFavorites().toList().sorted()
    }

    fun addFavorite(groupName: String) {
        val current = getFavorites().toMutableSet()
        current.add(groupName)
        sharedPref.edit {
            putStringSet(FAVORITES_KEY, current)
        }
        _favoriteGroups.value = current
    }

    fun removeFavorite(groupName: String) {
        val current = getFavorites().toMutableSet()
        current.remove(groupName)
        sharedPref.edit {
            putStringSet(FAVORITES_KEY, current)
        }
        _favoriteGroups.value = current
    }
}