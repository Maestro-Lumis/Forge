package com.application.forge

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.application.forge.di.initKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        enableEdgeToEdge()

        initKoin()

        setContent {
            App()
        }
    }
}