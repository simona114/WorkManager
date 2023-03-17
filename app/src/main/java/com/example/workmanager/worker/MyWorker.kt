package com.example.workmanager.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanager.MainActivity
import com.example.workmanager.R

class MyWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 111111

        const val WORK_NAME = "notification work :check"
    }

    /*
    Result has 3 states
    Success - work finished successfully
    Failure - work failed
    Retry - work failed and should be tried at another time, according to its retry policy
     */
    override fun doWork(): Result {
        Log.d("doWork", "doWork: Success ")
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notification = Notification.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("A new task")
            .setContentText("Notification text")
            .setPriority(Notification.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "channel name"
            val channelDescription = "channel description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }

}