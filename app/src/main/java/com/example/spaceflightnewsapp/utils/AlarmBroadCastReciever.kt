package com.example.spaceflightnewsapp.utils

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.ui.MainActivity
import com.example.spaceflightnewsapp.utils.Constants.Companion.CHANNEL_ID
import com.example.spaceflightnewsapp.utils.Constants.Companion.CHANNEL_NAME
import java.util.*

class AlarmBroadCastReciever : BroadcastReceiver() {
    lateinit var mediaPlayer : MediaPlayer
    override fun onReceive(p0: Context?, p1: Intent?) {
        mediaPlayer = MediaPlayer.create(p0,Settings.System.DEFAULT_ALARM_ALERT_URI)
        //mediaPlayer.duration
            //Log.e("info", i.toString())
            mediaPlayer.start()

        Log.e("App Alarm", "Alarm just fired")


        createNotificationChannel(p0)
        val intent = Intent(p0, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(p0).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(p0!!,CHANNEL_ID)
            .setContentTitle("Reminder!")
            .setContentText("Reminder to see the Rocket Launch set via Space News")
            .setSmallIcon(R.drawable.ic_launch)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManager = NotificationManagerCompat.from(p0)
        notificationManager.notify(Constants.NOTIFICATION_ID,notification)

    }

    fun createNotificationChannel(context: Context?){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
                .apply {
                    // to do something like flash led etc.
                }
            val manager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}