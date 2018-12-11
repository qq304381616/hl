package com.hl.dotime

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(Runnable {
            Thread.sleep(200)
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }).start()
    }
}
