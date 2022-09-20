package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

        val fileName = intent.getStringExtra(Constants.KEY_OF_STATUS)
        fileNameValue.text = fileName

        val status = intent.getStringExtra(Constants.KEY_OF_FILENAME)
        statusValue.text = status

        ok.setOnClickListener {
            finish()
        }

    }


}
