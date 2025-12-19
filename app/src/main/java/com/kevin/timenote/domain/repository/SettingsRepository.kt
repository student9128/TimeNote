package com.kevin.timenote.domain.repository

import com.kevin.timenote.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeMode: Flow<ThemeMode>
    suspend fun changeThemeMode(mode: ThemeMode)
}