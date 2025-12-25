@file:OptIn(ExperimentalTime::class)

package com.kevin.timenote.common.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * 为 kotlinx.datetime.LocalDate 添加自定义格式化扩展函数
 * @param pattern 格式化字符串，如 "yyyy/MM/dd" 或 "MM月dd日"
 */
fun LocalDate.formatWithPattern(pattern: String): String {
    return try {
        // 使用缓存或直接创建 Formatter
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
        // 桥接到 Java 8 的 LocalDate 进行格式化
        this.toJavaLocalDate().format(formatter)
    } catch (e: Exception) {
        // 如果 pattern 非法，返回默认 toString() 避免崩溃
        this.toString()
    }
}

/**
 * 人性化日期显示
 */
fun LocalDate.toFriendlyString(): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val tomorrow = today.plus(1, DateTimeUnit.DAY)
    val yesterday = today.minus(1, DateTimeUnit.DAY)

    return when (this) {
        today -> "今天"
        tomorrow -> "明天"
        yesterday -> "昨天"
        else -> this.formatWithPattern("yyyy年MM月dd日")
    }
}

fun Long.toFriendlyString(): String {
    // 1. 将时间戳转换为当前时区的 LocalDate
    val targetDate = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    // 2. 调用 LocalDate 的逻辑
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val tomorrow = today.plus(1, DateTimeUnit.DAY)
    val yesterday = today.minus(1, DateTimeUnit.DAY)

    return when (targetDate) {
        today -> "今天"
        tomorrow -> "明天"
        yesterday -> "昨天"
        else -> targetDate.formatWithPattern("yyyy年MM月dd日")
    }
}

/**
 * 计算距离目标日期的剩余天数
 */
fun LocalDate.daysUntilTarget(): Long {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    return today.daysUntil(this).toLong()
}

fun Long.daysUntilTarget(): Long {
    val targetDate = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    return today.daysUntil(targetDate).toLong()
}

object DateFormatterCache {
    private const val MAX_ENTRIES = 50

    // 使用 LinkedHashMap 的构造函数实现 LRU (最近最少使用) 缓存
    private val cache =
        object : LinkedHashMap<String, DateTimeFormatter>(MAX_ENTRIES, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, DateTimeFormatter>?): Boolean {
                return size > MAX_ENTRIES // 当超过 50 个时，删除最旧的
            }
        }

    fun get(pattern: String): DateTimeFormatter {
        return synchronized(cache) {
            cache.getOrPut(pattern) {
                DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            }
        }
    }
}

fun Long.formatWithPattern(pattern: String): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val javaDateTime =
        instant.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    return javaDateTime.format(DateFormatterCache.get(pattern))
}

/**
 * 通用的日期格式化扩展函数
 * @param pattern 格式字符串，如 "yyyy/MM/dd HH:mm"
 */
//fun Long.formatWithPattern(pattern: String): String {
//    // 1. 将 Long 时间戳转为 kotlinx.datetime.Instant
//    val instant = Instant.fromEpochMilliseconds(this)
//
//    // 2. 转为系统时区的 LocalDateTime
//    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
//
//    // 3. 桥接到 Java Time 进行格式化
//    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
//    return localDateTime.toJavaLocalDateTime().format(formatter)
//}

// 针对 LocalDateTime 的重载
fun LocalDateTime.formatWithPattern(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return this.toJavaLocalDateTime().format(formatter)
}

/**
 * 将字符串日期从一种格式转换为另一种格式
 * @param inputDate 输入的日期字符串 (如 "2025-12-23")
 * @param inputPattern 输入日期的格式 (如 "yyyy-MM-dd")
 * @param outputPattern 输出日期的格式 (如 "yyyy/MM/dd")
 */
fun convertDateFormat(
    inputDate: String,
    inputPattern: String,
    outputPattern: String
): String {
    return try {
        // 1. 获取输入格式的解析器
        val inputFormatter = DateFormatterCache.get(inputPattern)

        // 2. 将字符串解析为 Java 的 LocalDate (或者 LocalDateTime)
        val date = java.time.LocalDate.parse(inputDate, inputFormatter)

        // 3. 获取输出格式的格式化器
        val outputFormatter = DateFormatterCache.get(outputPattern)

        // 4. 转换并返回
        date.format(outputFormatter)
    } catch (e: Exception) {
        // 如果解析失败（格式不匹配），返回原字符串或空
        inputDate
    }
}

/**
 * @param from inputPattern
 * @param to outputPattern
 */
fun String.reformatDate(from: String, to: String): String {
    return convertDateFormat(this, from, to)
}

fun Long.getStartOfDay(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}