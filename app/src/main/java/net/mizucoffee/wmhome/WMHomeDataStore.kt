package net.mizucoffee.wmhome

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "state")
val IS_TILE_SET = booleanPreferencesKey("isTileSet")

class HomeDataStore(private val context: Context) {
    suspend fun setIsTileSet(isTileSet: Boolean) {
        context.dataStore.edit { settings ->
            settings[IS_TILE_SET] = isTileSet
        }
    }

    suspend fun getIsTileSet(): Boolean {
        val isTileSetFlow: Flow<Boolean> = context.dataStore.data
            .map {
                it[IS_TILE_SET] ?: false
            }
        return isTileSetFlow.first()
    }
}