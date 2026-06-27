package com.example.frontend.data.local.pref

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    // Skeleton
}
