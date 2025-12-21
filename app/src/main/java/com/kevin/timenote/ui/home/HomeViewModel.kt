package com.kevin.timenote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.timenote.domain.usecase.CountdownUseCase
import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.todayIn
import java.util.Date
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val countdownUseCase: CountdownUseCase
) : ViewModel() {
    private val _dateLunar = MutableStateFlow("")
    val dateLunar = _dateLunar.asStateFlow()
    val _dateJieQi = MutableStateFlow("")
    val dateJieQi = _dateJieQi.asStateFlow()

    init {
//        val now: Instant = Clock.System.now()
//        val today: LocalDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
// or shorter
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val lunar = Solar.fromYmd(today.year, today.month.number, today.day).lunar
        _dateLunar.update {
            "${lunar}"
        }
        _dateJieQi.update { lunar.jieQi }
    }

    val countdowns = countdownUseCase.observeCountdowns()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
}
