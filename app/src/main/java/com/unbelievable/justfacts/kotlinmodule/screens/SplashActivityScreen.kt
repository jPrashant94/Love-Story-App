package com.unbelievable.justfacts.kotlinmodule.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.unbelievable.justfacts.GDPR
import com.unbelievable.justfacts.R
import com.unbelievable.justfacts.kotlinmodule.StoryApplication
import com.unbelievable.justfacts.kotlinmodule.nav.NavScreens
import com.unbelievable.justfacts.kotlinmodule.nav.findActivity
import kotlinx.coroutines.delay

@Composable
fun SplashScreenUI(navConteoller: NavController) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val gdpr = GDPR(LocalActivity.current)
    gdpr.updateGDPRConsentStatus()

    LaunchedEffect(Unit) {
        delay(8000)

        val application = context.applicationContext
        if (application !is StoryApplication || activity == null) {
            navConteoller.navigate(NavScreens.MAIN) {
                popUpTo(NavScreens.SPLASH) { inclusive = true }
            }
            return@LaunchedEffect
        }

        application.showAdIfAvailable(
            activity,
            object : StoryApplication.OnShowAdCompleteListener {
                override fun onShowAdComplete() {
                    navConteoller.navigate(NavScreens.MAIN) {
                        popUpTo(NavScreens.SPLASH) { inclusive = true }
                    }
                }
            })

    }

    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.round_icon),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .padding(16.dp)
            )
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(0.8f),
                trackColor = Color.White,
                color = Color.Green,
            )
        }

    }
}