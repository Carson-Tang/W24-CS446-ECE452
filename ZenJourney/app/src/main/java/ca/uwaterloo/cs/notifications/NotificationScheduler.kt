package ca.uwaterloo.cs.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import ca.uwaterloo.cs.AppState

// https://developer.android.com/develop/background-work/services/alarms/schedule
// https://developer.android.com/develop/ui/views/notifications/build-notification
object NotificationScheduler {
    private lateinit var notificationIntent: PendingIntent
    val isCustomDefaultNotificationSet get() = ::notificationIntent.isInitialized

    fun scheduleNotification(context: Context, hour: Int, min: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationIntent = Intent(context, NotificationReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, min)
            if (!after(Calendar.getInstance())) {
                // schedule for tomorrow if specified time has passed
                add(Calendar.DATE, 1)
            }
        }

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, notificationIntent),
            notificationIntent
        )
    }

    fun pauseNotification(appState: AppState) {
        val alarmManager = appState.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (isCustomDefaultNotificationSet) {
            alarmManager.cancel(notificationIntent)
        }
    }
}