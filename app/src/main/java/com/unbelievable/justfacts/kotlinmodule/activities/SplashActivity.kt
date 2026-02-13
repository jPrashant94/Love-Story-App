package com.unbelievable.justfacts.kotlinmodule.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.unbelievable.justfacts.kotlinmodule.model.StoryRepository
import com.unbelievable.justfacts.kotlinmodule.nav.AppNavigation
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppNavigation()
        }

    }
}

