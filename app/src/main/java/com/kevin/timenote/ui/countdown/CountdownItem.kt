package com.kevin.timenote.ui.countdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.layout.Spacer
import com.kevin.timenote.common.util.daysUntilTarget
import com.kevin.timenote.common.util.formatWithPattern
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.ui.theme.cornerCard
import com.kevin.timenote.ui.theme.padding10
import com.kevin.timenote.ui.theme.uniformPadding
import com.nlf.calendar.Solar
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.time.LocalDateTime
import java.util.Date
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
    val date = model.date
    val days = model.date.daysUntilTarget()
    val title = model.title
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = uniformPadding, vertical = 5.dp)
            .clip(RoundedCornerShape(cornerCard))
            .background(
//                shape = RoundedCornerShape(cornerCard),
                color = MaterialTheme.colorScheme.onPrimary
            )
            .drawBehind({
                val strokeWidth = cornerCard.toPx()
                drawLine(
                    color = Color(model.eventTypeColor),
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
            })
            .padding(uniformPadding)
    ) {
        Column(
            modifier = Modifier
//                .clickable(onClick = onClick)
//                .padding(horizontal = 16.dp, vertical = 5.dp)
//                .background(
//                    MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(
//                        cornerCard
//                    )
//                )
//                .padding(16.dp)

        ) {
            Text(
                "${model.title}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                if (!model.isLunar) {
                    "日期：${model.date.formatWithPattern("yyyy年MM月dd日")}"
                } else {
                    "日期：农历${model.lunarDate}"
                }
            )
            Text(processEventTitle(days, title, model.eventTypeName))
        }
        if (days < 0) {
            Box(
                Modifier
                    .background(Color(0xffF5F2F2), shape = CircleShape)
                    .width(5.dp)
                    .height(5.dp)
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
fun processEventTitle(days: Long, title: String, event: String): AnnotatedString {
    val highlightStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
//        color = Color(model.eventTypeColor) // 这里使用了事件类型对应的颜色
        // 如果想固定为红色，可以改成: color = Color.Red
    )
    val absDays = abs(days)
    return buildAnnotatedString {
        when (event) {
            "倒数日", "纪念日" -> {
                if (days >= 0) {
                    append("距离 ")
//                    withStyle(highlightStyle) { append(title) }
                    append(title)
                    append(" 还有 ")
                    withStyle(highlightStyle) { append(absDays.toString()) }
                    append(" 天")
                } else {
                    append(title)
                    append(" 已经过去 ")
                    withStyle(highlightStyle) { append(absDays.toString()) }
                    append(" 天")
                }
            }

            "生日" -> {
                if (days >= 0) {
                    append("距离 ")
                    append(title)
                    append(" 生日还有 ")
                    withStyle(highlightStyle) { append(absDays.toString()) }
                    append(" 天")
                } else {
                    append(title)
                    append(" 已经过去 ")
                    withStyle(highlightStyle) { append(absDays.toString()) }
                    append(" 天")
                }
            }

            else -> {
                withStyle(highlightStyle) { append(title) }
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