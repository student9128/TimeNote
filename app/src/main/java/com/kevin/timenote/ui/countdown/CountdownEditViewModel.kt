package com.kevin.timenote.ui.countdown

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.usecase.CountdownUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.google.gson.Gson

@HiltViewModel
class CountdownEditViewModel @Inject constructor(
    private val useCase: CountdownUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = mutableStateOf(CountdownEditUiState())
    val uiState: State<CountdownEditUiState> = _uiState

    init {
        savedStateHandle.get<String>("model")?.let {
            val model = Gson().fromJson(it, CountdownModel::class.java)
            _uiState.value = CountdownEditUiState(
                id = model.id,
                title = model.title,
                location = model.location,
                type = model.type,
                startTime = model.startTime,
                endTime = model.endTime
            )
        }
    }

    fun updateTitle(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun save(onFinish: () -> Unit) {
        viewModelScope.launch {
            val s = _uiState.value
            useCase.save(
                CountdownModel(
                    id = s.id,
                    title = s.title,
                    location = s.location,
                    type = s.type,
                    startTime = s.startTime,
                    endTime = s.endTime
                )
            )
            onFinish()
        }
    }

    fun delete(onFinish: () -> Unit) {
        viewModelScope.launch {
            val s = _uiState.value
            useCase.delete(
                CountdownModel(
                    id = s.id,
                    title = s.title,
                    location = s.location,
                    type = s.type,
                    startTime = s.startTime,
                    endTime = s.endTime
                )
            )
            onFinish()
        }
    }
}

