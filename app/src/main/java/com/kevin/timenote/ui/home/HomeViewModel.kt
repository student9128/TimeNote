package com.kevin.timenote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.timenote.domain.usecase.CountdownUseCase
import com.nlf.calendar.Solar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import java.util.Calendar
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val countdownUseCase: CountdownUseCase
) : ViewModel() {
    private val _dateLunar = MutableStateFlow("")
    val dateLunar = _dateLunar.asStateFlow()
    private val _dateJieQi = MutableStateFlow("")
    val dateJieQi = _dateJieQi.asStateFlow()

    private val _filterType = MutableStateFlow(0) // 0: 全部, 1: 今天, 2: 过去, 3: 未来
//  本地筛选
private val _allCountdowns = countdownUseCase.observeCountdowns()
    .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )
    val filteredCountdowns = combine(_allCountdowns, _filterType) { list, filter ->
        val now = System.currentTimeMillis()
        val todayStart = getStartOfDay(now)
        val todayEnd = getEndOfDay(now)

        when (filter) {
            1 -> list.filter { it.date in todayStart..todayEnd } // 今天
            2 -> list.filter { it.date < todayStart } // 过去
            3 -> list.filter { it.date > todayEnd } // 未来
            else -> list // 全部
        }
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5_000),
        emptyList())
    // 使用 flatMapLatest 监听 filterType 变化，并切换到对应的数据库查询流
//    val filteredCountdowns = _filterType.flatMapLatest { filter ->
//        val now = System.currentTimeMillis()
//        val todayStart = getStartOfDay(now)
//        val todayEnd = getEndOfDay(now)
//
//        when (filter) {
//            1 -> countdownUseCase.observeByDateRange(todayStart, todayEnd) // 今天
//            2 -> countdownUseCase.observePast(todayStart) // 过去 (小于今天开始)
//            3 -> countdownUseCase.observeFuture(todayEnd) // 未来 (大于今天结束)
//            else -> countdownUseCase.observeCountdowns() // 全部
//        }
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5_000),
//        emptyList()
//    )

    init {
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val lunar = Solar.fromYmd(today.year, today.month.number, today.dayOfMonth).lunar
        _dateLunar.update {
            "${lunar}  星期${lunar.weekInChinese}"
        }
        _dateJieQi.update { lunar.jieQi }
    }

    fun updateFilter(index: Int) {
        _filterType.value = index
    }
    
    private fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getEndOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
}
