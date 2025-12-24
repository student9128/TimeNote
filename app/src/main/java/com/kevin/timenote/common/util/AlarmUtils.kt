package com.kevin.timenote.common.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.model.RepeatMode
import com.kevin.timenote.receiver.ReminderReceiver
import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar
import java.lang.System
import java.util.Calendar
import java.util.Date

object AlarmUtils {

    fun scheduleAlarm(context: Context, model: CountdownModel) {
        if (!model.remind) {
            cancelAlarm(context, model.id)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 检查权限 (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w("AlarmUtils", "Cannot schedule exact alarms")
                // 这里应该引导用户去设置页开启权限，为了简化暂略
                return
            }
        }

        val triggerTime = if (model.isLunar) {
            val target = Calendar.getInstance().apply { timeInMillis = model.date }
            nextLunarTriggerTime(
                lunarKey = model.lunarDate,
                hour = target.get(Calendar.HOUR_OF_DAY),
                minute = target.get(Calendar.MINUTE)
            )
        } else {
            calculateTriggerTime(model)
        }
        val now = System.currentTimeMillis()

        Log.d(
            "AlarmUtils",
            "scheduleAlarm: title=${model.title}, triggerTime=$triggerTime, now=$now, date=${model.date}"
        )

        // 需求：如果是当天的那么保存后或者修改后立刻弹出一个通知

        // 修改策略：先判断是否“今天应该提醒”。
        if (shouldRemindToday(model)) {
            // 如果是今天应该提醒
            // 检查今天的提醒时间点是否已过
            val todayTriggerTime = getTodayTriggerTime(model)
            if (todayTriggerTime <= now) {
                // 时间已过，立即通知
                Log.d("AlarmUtils", "Triggering immediate notification for today")
                val intent = Intent(context, ReminderReceiver::class.java).apply {
                    putExtra("eventId", model.id)
                    putExtra("title", model.title)
                    putExtra("eventName", model.eventTypeName)
                }
                context.sendBroadcast(intent)

                // 如果是不重复的，就不用设闹钟了
                if (model.repeatMode == RepeatMode.ONCE) {
                    return
                }
            } else {
                // 时间没过，那就设置今天的闹钟
            }
        }

        // 无论是否立即通知了，只要是重复的，或者虽然是不重复但时间在未来，都需要设置闹钟。

        if (triggerTime > now) {
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("eventId", model.id)
                putExtra("title", model.title)
                putExtra("eventName", model.eventTypeName)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                model.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
                Log.d("AlarmUtils", "Alarm scheduled for event ${model.title} at $triggerTime")
            } catch (e: SecurityException) {
                Log.e("AlarmUtils", "Security exception when scheduling alarm", e)
            }
        }
    }

    fun cancelAlarm(context: Context, eventId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun getTodayTriggerTime(model: CountdownModel): Long {
        val calendar = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = model.date }
        copyTime(target, calendar)
        return calendar.timeInMillis
    }

    private fun shouldRemindToday(model: CountdownModel): Boolean {
        if (model.isLunar) {
            val todayLunar = Lunar.fromDate(Date(System.currentTimeMillis())).toString()
            return todayLunar == model.lunarDate
        }
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = model.date }

        return when (model.repeatMode) {
            RepeatMode.ONCE -> {
                isSameDay(now, target)
            }

            RepeatMode.DAILY -> true
            RepeatMode.MONTHLY -> now.get(Calendar.DAY_OF_MONTH) == target.get(Calendar.DAY_OF_MONTH)
            RepeatMode.YEARLY -> (now.get(Calendar.MONTH) == target.get(Calendar.MONTH)) &&
                    (now.get(Calendar.DAY_OF_MONTH) == target.get(Calendar.DAY_OF_MONTH))
        }
    }

    private fun calculateTriggerTime(model: CountdownModel): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = model.date

        val targetTime = calendar.timeInMillis
        val now = System.currentTimeMillis()

        // 如果目标时间 > 现在，直接返回
        if (targetTime > now) return targetTime

        // 如果目标时间 <= 现在，根据重复模式找下一次
        return when (model.repeatMode) {
            RepeatMode.ONCE -> targetTime // 已过去
            RepeatMode.DAILY -> {
                val nextCalendar = Calendar.getInstance()
                val target = Calendar.getInstance().apply { timeInMillis = model.date }
                copyTime(target, nextCalendar)

                if (nextCalendar.timeInMillis <= now) {
                    nextCalendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                nextCalendar.timeInMillis
            }

            RepeatMode.MONTHLY -> {
                val nextCalendar = Calendar.getInstance()
                val target = Calendar.getInstance().apply { timeInMillis = model.date }
                val targetDay = target.get(Calendar.DAY_OF_MONTH)

                // Copy time
                copyTime(target, nextCalendar)

                if (nextCalendar.get(Calendar.DAY_OF_MONTH) > targetDay ||
                    (nextCalendar.get(Calendar.DAY_OF_MONTH) == targetDay && nextCalendar.timeInMillis <= now)
                ) {
                    nextCalendar.add(Calendar.MONTH, 1)
                }

                // 设置为目标日
                nextCalendar.set(Calendar.DAY_OF_MONTH, targetDay)
                copyTime(target, nextCalendar)

                // 确保是未来
                while (nextCalendar.timeInMillis <= now) {
                    nextCalendar.add(Calendar.MONTH, 1)
                    nextCalendar.set(Calendar.DAY_OF_MONTH, targetDay)
                    copyTime(target, nextCalendar)
                }
                nextCalendar.timeInMillis
            }

            RepeatMode.YEARLY -> {
                val targetCal = Calendar.getInstance().apply { timeInMillis = model.date }
                val nextCalendar = Calendar.getInstance()

                nextCalendar.set(Calendar.MONTH, targetCal.get(Calendar.MONTH))
                nextCalendar.set(Calendar.DAY_OF_MONTH, targetCal.get(Calendar.DAY_OF_MONTH))
                copyTime(targetCal, nextCalendar)

                if (nextCalendar.timeInMillis <= now) {
                    nextCalendar.add(Calendar.YEAR, 1)
                }
                nextCalendar.timeInMillis
            }
        }
    }
    private fun nextLunarTriggerTime(
        lunarKey: String,
        hour: Int,
        minute: Int
    ): Long {
        val (lunarMonth, lunarDay) = lunarKey.split("-").map { it.toInt() }

        val now = Calendar.getInstance()
        var year = now.get(Calendar.YEAR)

        while (true) {
            val lunar = com.nlf.calendar.Lunar.fromYmd(year, lunarMonth, lunarDay)
            val solar = lunar.solar

            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, solar.year)
                set(Calendar.MONTH, solar.month - 1)
                set(Calendar.DAY_OF_MONTH, solar.day)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (cal.timeInMillis > System.currentTimeMillis()) {
                return cal.timeInMillis
            }

            year++
        }
    }


    private fun copyTime(from: Calendar, to: Calendar) {
        to.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY))
        to.set(Calendar.MINUTE, from.get(Calendar.MINUTE))
        to.set(Calendar.SECOND, from.get(Calendar.SECOND))
        to.set(Calendar.MILLISECOND, from.get(Calendar.MILLISECOND))
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
