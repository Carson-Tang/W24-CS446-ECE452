package ca.uwaterloo.cs.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ca.uwaterloo.cs.MainActivity
import ca.uwaterloo.cs.MainActivity.Companion.appState
import ca.uwaterloo.cs.R
import ca.uwaterloo.cs.customAffirmations
import ca.uwaterloo.cs.randAffirmations
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

// https://developer.android.com/reference/android/content/BroadcastReceiver
class NotificationReceiver() : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        runBlocking {
            sendNotification(context)
        }
    }

    private suspend fun sendNotification(context: Context) {
        val showHomePageIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                showHomePageIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        val notificationBuilder = NotificationCompat.Builder(context, "1")
            .setContentTitle("ZenJourney")
            .setContentText(getCustomAffirmation())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.pikachu_3)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, notificationBuilder.build())
        }
        NotificationScheduler.scheduleNotification(context, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE))
    }

    private suspend fun getCustomAffirmation(): String? {
        val currentDate = LocalDate.now()
        val yesterday = LocalDate.now().minusDays(1)
        try {
            val response = appState.userStrategy?.getJournalByDate(
                appState = appState,
                day = currentDate.dayOfMonth,
                month = currentDate.monthValue,
                year = currentDate.year
            )
            if (response != null) {
                val validMoods = response.moods.filter { customAffirmations.keys.contains(it) }
                return customAffirmations[validMoods.random()]?.random()
            }
        } catch (e: Exception) {
            println(e.message)
        }
        try {
            val response = appState.userStrategy?.getJournalByDate(
                appState = appState,
                day = yesterday.dayOfMonth,
                month = yesterday.monthValue,
                year = yesterday.year
            )
            if (response != null) {
                val validMoods = response.moods.filter { customAffirmations.keys.contains(it) }
                return customAffirmations[validMoods.random()]?.random()
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return randAffirmations.random()
    }
}