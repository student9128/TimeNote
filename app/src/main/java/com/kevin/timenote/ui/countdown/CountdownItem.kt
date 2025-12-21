package com.kevin.timenote.ui.countdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.layout.Spacer
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.ui.theme.cornerCard
import com.kevin.timenote.ui.theme.padding10
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.text.append
import kotlin.text.format
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun CountdownItem(
    model: CountdownModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .background(
                MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(
                    cornerCard
                ))
            .padding(16.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(processEventTitle(model))
            Spacer(Modifier.width(5.dp))
            Box(
                Modifier
                    .background(Color(model.eventTypeColor), shape = CircleShape)
                    .width(5.dp)
                    .height(5.dp)
            )
        }
        Text("目标日期${formatDate(model.date)}")

    }
}

@OptIn(ExperimentalTime::class)
fun processEventTitle(model: CountdownModel): AnnotatedString {
    // 1. 获取当前系统时区
    val timeZone = TimeZone.currentSystemDefault()

    // 2. 获取当前的日期 (LocalDate)
    val today: LocalDate = Clock.System.todayIn(kotlinx.datetime.TimeZone.currentSystemDefault())

    // 3. 将目标 Long 时间戳转为 LocalDate
    // 注意：如果是毫秒级时间戳 (Milliseconds)，直接用 fromEpochMilliseconds
    // 如果是秒级 (Seconds)，用 fromEpochSeconds
    val targetDate = Instant.fromEpochMilliseconds(model.date)
        .toLocalDateTime(timeZone)
        .date

    // 4. 计算天数差 (today 到 targetDate 的距离)
    val days = today.daysUntil(targetDate)
    val highlightStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
//        color = Color(model.eventTypeColor) // 这里使用了事件类型对应的颜色
        // 如果想固定为红色，可以改成: color = Color.Red
    )
    val absDays = abs(days)
    return buildAnnotatedString {
        when (model.eventTypeName) {
            "倒数日", "纪念日" -> {
                if (days >= 0) {
                    append("距离 ")
                    withStyle(highlightStyle) { append(model.title) }
                    append(" 还有 ")
                    withStyle(highlightStyle) { append(absDays.toString()) }
                    append(" 天")
                } else {
                    withStyle(highlightStyle) { append(model.title) }
                    append(" 已经过去 ")
                    withStyle(highlightStyle) { append(absDays.toString()) }
                    append(" 天")
                }
            }

            "生日" -> {
                append("距离 ")
                withStyle(highlightStyle) { append(model.title) }
                append(" 生日还有 ")
                withStyle(highlightStyle) { append(absDays.toString()) }
                append(" 天")
            }

            else -> {
                withStyle(highlightStyle) { append(model.title) }
                append(" ")
                withStyle(highlightStyle) { append(days.toString()) }
                append(" 天")
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
fun formatDate(timestamp: Long): String {
    // 1. 将 Long (毫秒) 转为 LocalDateTime
    val dateTime = Instant.fromEpochMilliseconds(timestamp)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    // 2. 定义格式 (例如: 2023-12-25)
    // kotlinx-datetime 0.6.0+ 推荐使用这种 DSL 方式定义格式


    // 3. 返回格式化后的字符串
    return "${dateTime.year}年${dateTime.month}月${dateTime.day}日"
}