package com.example.rafaelmarra.pertubis.view

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.rafaelmarra.pertubis.R
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
}
