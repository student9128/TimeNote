package com.kevin.timenote.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kevin.timenote.domain.model.ThemeMode
import com.kevin.timenote.domain.repository.SettingsRepository
import com.kevin.timenote.extension.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl@Inject constructor(
    @ApplicationContext private val context: Context,
)  : SettingsRepository {
    private val themeModeKey = stringPreferencesKey("ThemeMode")

    // 1. 读取流：监听设置的变化
    override val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val modeName = preferences[themeModeKey] ?: ThemeMode.Light.name
        try {
            ThemeMode.valueOf(modeName)
        } catch (e: Exception) {
            ThemeMode.Light // 默认兜底值
        }
    }

    override suspend fun changeThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[themeModeKey]=mode.name
        }
    }
}