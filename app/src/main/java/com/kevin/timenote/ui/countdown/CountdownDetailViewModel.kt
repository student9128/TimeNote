package com.kevin.timenote.ui.countdown

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevin.timenote.common.util.AlarmUtils
import com.kevin.timenote.common.util.TimeL
import com.kevin.timenote.common.util.TimeL.printD
import com.kevin.timenote.data.mapper.toEditState
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.model.RepeatMode
import com.kevin.timenote.domain.usecase.CountdownUseCase
import com.kevin.timenote.ui.navigation.TimeRoute
import com.nlf.calendar.Solar
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CountdownDetailViewModel @Inject constructor(
    private val countdownUseCase: CountdownUseCase,
    @param:ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(CountdownEditUiState())
    private val route = savedStateHandle.toRoute<TimeRoute.CountdownDetail>()
    val uiState: StateFlow<CountdownEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // 假设 getCountdownById 返回的是 Flow<CountdownModel?> 或 Flow<CountdownModel>
//            val model = countdownUseCase.getCountdownById(route.id).firstOrNull()
//            if (model != null) {
//                _uiState.update { currentState ->
//                    currentState.copy(
//                        id = model.id,
//                        title = model.title,
//                        location = model.location,
//                        type = model.type,
//                        startTime = model.startTime,
//                        endTime = model.endTime,
//                        date = model.date,
//                        lunarDate = model.lunarDate,
//                        isLunar = model.isLunar,
//                        eventTypeName = model.eventTypeName,
//                        eventTypeColor = model.eventTypeColor,
//                        repeatMode = model.repeatMode,
//                        remind = model.remind
//                    )
//                }
//            }
            countdownUseCase.getCountdownById(route.id)
                .takeWhile { it != null }
//                .filterNotNull()
                .collect { model ->
                    // 每次数据库数据变更，这里都会收到通知
                    if (model == null) {
                        return@collect
                    }
                    printD { "isLunar===${model.isLunar},${model.lunarDate}" }
                    _uiState.update { model.toEditState()
                    }

                }
        }
    }

    fun updateState(transform: (CountdownEditUiState) -> CountdownEditUiState) {
        // 使用 .update 保证并发更新时的原子性 (Atomic)
        _uiState.update { transform(it) }
    }

    fun updateRepeatMode(repeatMode: RepeatMode) {
        _uiState.update { it.copy(repeatMode = repeatMode) }
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

            // For updates, we usually keep the same ID, so we can schedule alarm directly using it.
            // But if it's new (which shouldn't happen often in Detail screen, but let's be safe),
            // we should get the ID.

            // Assuming save logic handles both insert/update.
            // Since we are in DetailVM, it's likely an update to existing ID.
            countdownUseCase.save(model)

            // Schedule/Reschedule Alarm
            AlarmUtils.scheduleAlarm(context, model)

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
}
