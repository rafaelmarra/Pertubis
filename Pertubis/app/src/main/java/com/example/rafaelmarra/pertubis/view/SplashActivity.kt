package com.example.rafaelmarra.pertubis.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.viewmodel.business.CHANNEL_ID
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        createNotificationChannel()
        startAnimation()
        startTimer()
    }

    private fun startAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.animation_blink)
        imgIconSplash.startAnimation(animation)
    }

    private fun startTimer() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(Intent(this@SplashActivity, ListaActivity::class.java))
                finish()
            }
        }, 5000)
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pertubis"
            val descriptionText = "Perturbação"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
