package com.unbelievable.justfacts.kotlinmodule.nav

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unbelievable.justfacts.kotlinmodule.model.LanguagePreferences
import com.unbelievable.justfacts.kotlinmodule.model.StoryViewModel
import com.unbelievable.justfacts.kotlinmodule.model.StoryViewModelFactory
import com.unbelievable.justfacts.kotlinmodule.screens.MainScreenUI
import com.unbelievable.justfacts.kotlinmodule.screens.ReadStoryScreenUI
import com.unbelievable.justfacts.kotlinmodule.screens.SplashScreenUI

@Composable
fun AppNavigation() {

    val context = LocalContext.current
    val prefs = remember { LanguagePreferences(context) }
    val viewModel: StoryViewModel = viewModel(
        factory = StoryViewModelFactory(prefs)
    )

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavScreens.SPLASH) {
        composable(NavScreens.SPLASH) {
            SplashScreenUI(navController)
        }
        composable(NavScreens.MAIN) {
            MainScreenUI(navController,storyModel = viewModel)
        }
        composable(
            "${NavScreens.READ_STORY}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )) { backStackEntry ->
            ReadStoryScreenUI(
                id = backStackEntry.arguments?.getInt("id") ?: 0,
                navController,
                storyModel = viewModel
            )
        }
    }


}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}