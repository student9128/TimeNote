package com.kevin.timenote.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kevin.timenote.domain.model.ThemeMode
import com.kevin.timenote.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "settings")
class SettingsRepositoryImpl@Inject constructor(
    @ApplicationContext private val context: Context,
)  : SettingsRepository {
    private val ThemeModeKey = stringPreferencesKey("ThemeMode")

    // 1. 读取流：监听设置的变化
    override val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val modeName = preferences[ThemeModeKey] ?: ThemeMode.Light.name
        try {
            ThemeMode.valueOf(modeName)
        } catch (e: Exception) {
            ThemeMode.Light // 默认兜底值
        }
    }

    override suspend fun changeThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[ThemeModeKey]=mode.name
        }
    }
}