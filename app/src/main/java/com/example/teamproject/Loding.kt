package com.example.teamproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class Loding : Activity() {

    private  val SPLASH_TIME_OUT:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }, SPLASH_TIME_OUT)

    }



}