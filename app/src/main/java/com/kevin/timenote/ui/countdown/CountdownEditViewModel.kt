package com.kevin.timenote.ui.countdown

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.usecase.CountdownUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.gson.Gson
import com.kevin.timenote.domain.usecase.EventTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class CountdownEditViewModel @Inject constructor(
    private val countdownUseCase: CountdownUseCase,
    private val eventTypeUseCase: EventTypeUseCase,
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
                eventTypeColor = model.eventTypeColor


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

    // 只保留这一个公开方法
    fun updateState(transform: (CountdownEditUiState) -> CountdownEditUiState) {
        // 使用 .update 保证并发更新时的原子性 (Atomic)
        _uiState.update { transform(it) }
    }



    fun save(onFinish: () -> Unit) {
        viewModelScope.launch {
            val s = _uiState.value
            countdownUseCase.save(
                CountdownModel(
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
                    eventTypeColor = s.eventTypeColor
                )
            )
            onFinish()
        }
    }

    fun delete(onFinish: () -> Unit) {
        viewModelScope.launch {
            val s = _uiState.value
            countdownUseCase.delete(
                CountdownModel(
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
                    eventTypeColor = s.eventTypeColor
                )
            )
            onFinish()
        }
    }
}

