package com.kevin.timenote.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kevin.timenote.MainActivity
import com.kevin.timenote.R
import com.kevin.timenote.domain.usecase.CountdownUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var countdownUseCase: CountdownUseCase

    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getLongExtra("eventId", -1)
        val title = intent.getStringExtra("title")
        val eventName = intent.getStringExtra("eventName")

        // 如果没有获取到有效信息（比如 id 为 -1 或者标题为空），则不显示通知
        // 主要是为了防止 App 启动或收到其他广播（如 BOOT_COMPLETED）时意外触发空通知
        if (eventId == -1L || (title.isNullOrEmpty() && eventName.isNullOrEmpty())) {
            return
        }

        // 显示通知
        showNotification(context, title ?: "提醒", eventName ?: "提醒", eventId)
    }

    private fun showNotification(context: Context, title: String, eventName: String, eventId: Long) {
        val channelId = "countdown_reminder_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "倒数日提醒",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(if (eventName != "提醒") "${eventName}提醒" else "倒数日提醒")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(eventId.toInt(), notification)
    }
}
