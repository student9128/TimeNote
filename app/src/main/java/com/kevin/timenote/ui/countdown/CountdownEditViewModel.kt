package com.kevin.timenote.ui.countdown

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.timenote.common.util.AlarmUtils
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.usecase.CountdownUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.gson.Gson
import com.kevin.timenote.TimeGlanceWidget
import com.kevin.timenote.domain.model.RepeatMode
import com.kevin.timenote.domain.usecase.EventTypeUseCase
import com.nlf.calendar.Solar
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Date

@HiltViewModel
class CountdownEditViewModel @Inject constructor(
    private val countdownUseCase: CountdownUseCase,
    private val eventTypeUseCase: EventTypeUseCase,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CountdownEditUiState())
    val uiState: StateFlow<CountdownEditUiState> = _uiState.asStateFlow()
    val eventTypes = eventTypeUseCase.observeAllTypes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    var selectedType by mutableStateOf("倒数日")

    init {
        savedStateHandle.get<String>("model")?.let {
            val model = Gson().fromJson(it, CountdownModel::class.java)
            Log.d("Count", "title=${model.title}")
            _uiState.value = CountdownEditUiState(
                id = model.id,
                title = model.title,
                location = model.location,
                type = model.type,
                startTime = model.startTime,
                endTime = model.endTime,
                date = model.date,
                lunarDate = model.lunarDate,
                isLunar = model.isLunar,
                eventTypeName = model.eventTypeName,
                eventTypeColor = model.eventTypeColor,
                repeatMode = model.repeatMode,
                remind = model.remind
            )
        }
        viewModelScope.launch { eventTypeUseCase.initDefaultTypes() }

    }

    fun updateSelectedType(type: String) {
        selectedType = type
        // 如果你需要同步更新到 countdownModel State，在这里操作
        // _state.update { it.copy(type = type) }
    }

    fun updateTitle(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun updateDate(value: Long) {
        _uiState.value = _uiState.value.copy(date = value)
    }

    fun updateEventTypeName(value: String) {
        _uiState.value = _uiState.value.copy(eventTypeName = value)
    }

    fun updateRepeatMode(value: RepeatMode) {
        _uiState.value = _uiState.value.copy(repeatMode = value)
    }

    // 只保留这一个公开方法
    fun updateState(transform: (CountdownEditUiState) -> CountdownEditUiState) {
        // 使用 .update 保证并发更新时的原子性 (Atomic)
        _uiState.update { transform(it) }
    }


    fun save(onFinish: () -> Unit) {
        viewModelScope.launch {
            val s = _uiState.value
            val model = CountdownModel(
                id = s.id,
                title = s.title,
                location = s.location,
                type = s.type,
                startTime = s.startTime,
                endTime = s.endTime,
                date = s.date,
                lunarDate = s.lunarDate,
                isLunar = s.isLunar,
                eventTypeName = s.eventTypeName,
                eventTypeColor = s.eventTypeColor,
                repeatMode = s.repeatMode,
                remind = s.remind
            )
            // Save returns the ID of the inserted/updated item (or we can get it differently)
            // But since we are saving via UseCase which might not return ID directly for update,
            // we should handle Insert vs Update logic.
            // Assuming save handles id=0 as insert.

            // For alarm scheduling, we need the ID.
            // If it's a new item (id=0), we need to get the generated ID.
            // The current usecase.save might need to be adjusted to return ID.
            val savedId = countdownUseCase.saveAndGetId(model)

            // Schedule Alarm
            val savedModel = model.copy(id = savedId)
            AlarmUtils.scheduleAlarm(context, savedModel)
            refreshWidgets()
            onFinish()
        }
    }

    fun delete(onFinish: () -> Unit) {
        viewModelScope.launch {
            val s = _uiState.value
            val model = CountdownModel(
                id = s.id,
                title = s.title,
                location = s.location,
                type = s.type,
                startTime = s.startTime,
                endTime = s.endTime,
                date = s.date,
                lunarDate = s.lunarDate,
                isLunar = s.isLunar,
                eventTypeName = s.eventTypeName,
                eventTypeColor = s.eventTypeColor,
                repeatMode = s.repeatMode,
                remind = s.remind
            )
            countdownUseCase.delete(model)
            AlarmUtils.cancelAlarm(context, s.id)
            onFinish()
        }
    }

    private suspend fun refreshWidgets() {
        val manager = GlanceAppWidgetManager(context)
        manager.getGlanceIds(TimeGlanceWidget::class.java).forEach { id ->
            TimeGlanceWidget().update(context, id)
        }
    }
}
