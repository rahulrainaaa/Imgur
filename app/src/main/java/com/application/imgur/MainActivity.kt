package com.application.imgur

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * https://github.com/rahulrainaaa/Imgur.git
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}

