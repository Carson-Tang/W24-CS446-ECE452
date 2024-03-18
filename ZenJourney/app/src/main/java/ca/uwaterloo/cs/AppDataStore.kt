package ca.uwaterloo.cs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStore(val context: Context) {
    // referenced from: https://developer.android.com/topic/libraries/architecture/datastore#kotlin
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "jwt"
    )

    suspend fun addToDataStore(jwt: String) {
        context.dataStore.edit { store ->
            store[stringPreferencesKey("jwt")] = jwt
        }
    }

    suspend fun getFromDataStore(): String {
        return context.dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey("jwt")] ?: ""
            }.first()
    }
}