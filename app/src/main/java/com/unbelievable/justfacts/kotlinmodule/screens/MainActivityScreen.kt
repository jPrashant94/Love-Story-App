package com.unbelievable.justfacts.kotlinmodule.screens

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.unbelievable.justfacts.R
import com.unbelievable.justfacts.kotlinmodule.ADConstant
import com.unbelievable.justfacts.kotlinmodule.model.MainModel
import com.unbelievable.justfacts.kotlinmodule.model.MainUIState
import com.unbelievable.justfacts.kotlinmodule.model.StoryModel
import com.unbelievable.justfacts.kotlinmodule.model.StoryViewModel
import com.unbelievable.justfacts.kotlinmodule.model.getTitle
import com.unbelievable.justfacts.kotlinmodule.nav.NavScreens

@Composable
fun MainScreenUI(
    navConteoller: NavController,
    storyModel: StoryViewModel,
    viewModel: MainModel = viewModel()
) {

    var count by remember { mutableStateOf(2) }

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalActivity.current as Activity
    ADConstant.LoadAdmobInsterstitialAd(context)

    val selectedLanguage by storyModel.selectedLanguage.collectAsState()

    println("MAIN== " + selectedLanguage)

    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(color = Color.Black)
    ) {

        when (uiState) {
            is MainUIState.Loading -> {
                LoadingScreenUI()
            }

            is MainUIState.Success -> {
                MainStoryScreen(
                    selectedLanguage,
                    storyModel,
                    stories = (uiState as MainUIState.Success).stories,
                ) { story ->
                    val showAd = viewModel.onStoryClicked(story)
                    val id = story.id
                    if (showAd) {
                        ADConstant.ShowAdmobInsterstitial(
                            context,
                            object : ADConstant.CallBack {
                                override fun MoveToNext() {
                                    navConteoller.navigate("${NavScreens.READ_STORY}/${id}")

                                }

                            })
                    } else {
                        navConteoller.navigate("${NavScreens.READ_STORY}/${id}")

                    }
                }
            }

        }

    }


}

@Composable
fun LoadingScreenUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(trackColor = Color.White, color = Color.Green)
        Text(
            text = "Please Wait...",
            color = Color.White,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStoryScreen(
    selectedLanguage: String,
    storyModel: StoryViewModel,
    stories: List<StoryModel>,
    onItemClick: (StoryModel) -> Unit
) {

    // State to control menu visibility
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Stories") },
                actions = {
                    // Language Icon Button
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.language),
                                contentDescription = "Change Language"
                            )
                        }

                        // Language Dropdown Menu
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("English") },
                                onClick = {
                                    storyModel.changeLanguage("en")
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("हिंदी (Hindi)") },
                                onClick = {
                                    storyModel.changeLanguage("hi")
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("ગુજરાતી (Gujarati)") },
                                onClick = {

                                    storyModel.changeLanguage("gu")
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        // Your existing StoryListUI inside the Scaffold's content slot
        Box(modifier = Modifier.padding(innerPadding)) {
            StoryListUI(
                stories = stories,
                selectedLanguage = selectedLanguage,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun StoryListUI(
    stories: List<StoryModel>,
    selectedLanguage: String,
    onItemClick: (StoryModel) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(10.dp)) {
        items(stories) { story ->


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onItemClick(story)
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.bg_1),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = story.getTitle(selectedLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)

                )
            }
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}
