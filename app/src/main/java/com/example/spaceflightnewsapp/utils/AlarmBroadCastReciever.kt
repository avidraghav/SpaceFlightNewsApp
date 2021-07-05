package com.example.spaceflightnewsapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.provider.Settings
import android.util.Log

class AlarmBroadCastReciever : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val mediaPlayer = MediaPlayer.create(p0,Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.start()

        Log.d("App Alarm", "Alarm just fired");
    }
}