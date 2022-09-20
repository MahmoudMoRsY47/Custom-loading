package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0


    private lateinit var ChooseURL :String
    private lateinit var nameOfFile: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            custom_button.buttonState = ButtonState.Loading
            download()

        }
        addChannel(CHANNEL_ID, "Channel Name")
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            custom_button.buttonState = ButtonState.Completed

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                var downloadStatus = "Fail"
                if (DownloadManager.STATUS_SUCCESSFUL == status) {
                    downloadStatus = "Success"
                }

                val toast = Toast.makeText(
                    applicationContext,
                    getString(R.string.notification_description),
                    Toast.LENGTH_LONG)
                toast.show()

                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.sendNotification(
                    CHANNEL_ID,
                    getString(R.string.notification_description),
                    applicationContext,
                    downloadStatus,
                    nameOfFile
                )
            }
        }
    }


    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }
    fun onRadioClicked(view: View) {
        if (view is RadioButton && view.isChecked) {

            when (view.getId()) {
                R.id.radio_glide ->
                    ChooseURL = URL
                R.id.radio_load_app ->
                    ChooseURL = URL2
                R.id.radio_retrofit ->
                    ChooseURL = URL3
            }

            nameOfFile = view.text.toString()
        }
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL2 =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/masterXXX.zip"
        private const val URL3 =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val CHANNEL_ID = "channel"
    }

    private fun addChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Channel Description"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
