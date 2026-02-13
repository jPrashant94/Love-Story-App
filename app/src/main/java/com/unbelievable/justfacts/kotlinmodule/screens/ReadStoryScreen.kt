package com.unbelievable.justfacts.kotlinmodule.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.unbelievable.justfacts.R
import com.unbelievable.justfacts.kotlinmodule.model.FontSizeModel
import com.unbelievable.justfacts.kotlinmodule.model.StoryModel
import com.unbelievable.justfacts.kotlinmodule.model.StoryRepository
import com.unbelievable.justfacts.kotlinmodule.model.StoryViewModel
import com.unbelievable.justfacts.kotlinmodule.model.ThemePreferences
import com.unbelievable.justfacts.kotlinmodule.model.getDesc
import com.unbelievable.justfacts.kotlinmodule.model.getTitle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReadStoryScreenUI(
    id: Int,
    navController: NavController,
    storyModel: StoryViewModel,
    fontModel: FontSizeModel = viewModel()

) {

    val selectedLanguage by storyModel.selectedLanguage.collectAsState()
    var localLanguage by rememberSaveable { mutableStateOf(selectedLanguage) }

    val story = StoryRepository.getStoryById(id)
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var story_title = story?.getTitle(localLanguage) ?: ""
    var story_content = story?.getDesc(localLanguage) ?: ""

    println("MAIN== "+localLanguage)

    val themeStore = remember { ThemePreferences(context) }
    val isNightMode by themeStore.darkModeFlow.collectAsState(initial = false)

    val fontState by fontModel.uiState.collectAsState()

    var isAutoScroll by rememberSaveable { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isAutoScroll) {
        while (isAutoScroll) {
            delay(40)
            scrollState.scrollBy(2f)
        }
    }

    val bgColor = if (isNightMode) Color.Black else Color.White
    val txtColor = if (isNightMode) Color.White else Color.Black


    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(bgColor)

    ) {
        Text(
            text = story_title,
            fontSize = 20.sp,
            color = txtColor,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            fontFamily = androidx.compose.ui.text.font.FontFamily(Font(R.font.mukta_regular)),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(8.dp)
        ) {
            Text(
                text = story_content,
                fontFamily = androidx.compose.ui.text.font.FontFamily(Font(R.font.mukta_regular)),
                fontSize = fontState.fontSize.sp,
                color = txtColor
            )
        }
        ControlBar(
            onShare = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "${story?.title_hn ?: ""}\n\n${story_content}")
                }
                context.startActivity(Intent.createChooser(intent, "Share"))
            },
            onFont = {
                fontModel.increaseFont()

            },
            onNightMode = {
                val newValue = !isNightMode
                scope.launch {
                    themeStore.saveDarkMode(newValue)
                }
            },
            onAutoScroll = { isAutoScroll = !isAutoScroll },
            onLangChange = { langCode ->
                when (langCode) {
                    "hn" -> {
                        localLanguage = "hi"
                    }

                    "en" -> {
                        localLanguage = "en"
                    }

                    "gj" -> {
                        localLanguage = "gj"
                    }
                }
//                selectedLanguage = langCode
            }
        )
        AdaptiveAdMobBanner(
            adUnitId = stringResource(R.string.admob_banner)
        )
    }
}

@Composable
fun ControlBar(
    onShare: () -> Unit,
    onFont: () -> Unit,
    onNightMode: () -> Unit,
    onAutoScroll: () -> Unit,
    onLangChange: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onShare) {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = null
                )
            }
            IconButton(onClick = onFont) {
                Icon(
                    painter = painterResource(id = R.drawable.font_size),
                    contentDescription = null
                )
            }
            IconButton(onClick = onNightMode) {
                Icon(
                    painter = painterResource(id = R.drawable.brightness),
                    contentDescription = null
                )
            }
            IconButton(onClick = onAutoScroll) {
                Icon(
                    painter = painterResource(id = R.drawable.scroll),
                    contentDescription = null
                )
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.language),
                        contentDescription = "Language"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text("Hindi") },
                        onClick = {
                            expanded = false
                            onLangChange("hn")
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("English") },
                        onClick = {
                            expanded = false
                            onLangChange("en")
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Gujarati") },
                        onClick = {
                            expanded = false
                            onLangChange("gj")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdaptiveAdMobBanner(adUnitId: String) {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val adWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()

    val adView = remember {
        AdView(context).apply {
            setAdSize(
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context, adWidth
                )
            )
            this.adUnitId = adUnitId
            loadAd(AdRequest.Builder().build())
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            adView.destroy()   // ✅ AUTO CLEANUP
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { adView }
    )
}