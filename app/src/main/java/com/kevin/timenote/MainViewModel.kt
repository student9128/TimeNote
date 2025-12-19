package com.kevin.timenote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.timenote.domain.model.ThemeMode
import com.kevin.timenote.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: SettingsRepository) : ViewModel() {
    // 将 Flow 转换为 StateFlow，这样 UI 一启动就能拿到当前状态
    val themeMode: StateFlow<ThemeMode> = repository.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeMode.Light // 初始值，数据加载完前暂时显示这个
        )

    // 提供给 UI 调用的修改方法
    fun updateTheme(mode: ThemeMode) {
        viewModelScope.launch {
            repository.changeThemeMode(mode)
        }
    }
}