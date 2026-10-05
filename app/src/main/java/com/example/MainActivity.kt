package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AppTabBar
import com.example.ui.screens.AvatarCreatorScreen
import com.example.ui.screens.DecorateScreen
import com.example.ui.screens.LudoGameScreen
import com.example.ui.screens.MailboxScreen
import com.example.ui.screens.MemoriesShelfScreen
import com.example.ui.screens.PlayHubScreen
import com.example.ui.screens.QuestionsDeckScreen
import com.example.ui.screens.QuietCompanyScreen
import com.example.ui.screens.TheRoomScreen
import com.example.ui.screens.TogetherScreen
import com.example.ui.screens.WelcomePairingScreen
import com.example.ui.theme.BetweenUsTheme
import com.example.ui.theme.PlumDeep
import com.example.viewmodel.BetweenUsViewModel

sealed class AppDestination {
    data object MainTabs : AppDestination()
    data object WelcomePairing : AppDestination()
    data object AvatarCreator : AppDestination()
    data object LudoGame : AppDestination()
    data object QuestionsDeck : AppDestination()
    data object DecorateRoom : AppDestination()
    data object QuietCompany : AppDestination()
}

class MainActivity : ComponentActivity() {

    private val viewModel: BetweenUsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BetweenUsTheme {
                MainBetweenUsApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainBetweenUsApp(viewModel: BetweenUsViewModel) {
    val currentProfile by viewModel.currentProfile.collectAsStateWithLifecycle()
    val partnerName = if (currentProfile == "Kola") "Joy" else "Kola"

    var currentDestination by remember { mutableStateOf<AppDestination>(AppDestination.MainTabs) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Room, 1: Play, 2: Mail, 3: Together, 4: Shelf
    var partnerIsAsleep by remember { mutableStateOf(false) }

    // System BackHandler
    BackHandler(enabled = currentDestination != AppDestination.MainTabs || selectedTab != 0) {
        if (currentDestination != AppDestination.MainTabs) {
            currentDestination = AppDestination.MainTabs
        } else if (selectedTab != 0) {
            selectedTab = 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlumDeep)
            .navigationBarsPadding()
    ) {
        Crossfade(targetState = currentDestination, label = "destination_crossfade") { dest ->
            when (dest) {
                AppDestination.MainTabs -> {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = PlumDeep,
                        bottomBar = {
                            AppTabBar(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Crossfade(targetState = selectedTab, label = "tabs_crossfade") { tab ->
                                when (tab) {
                                    0 -> TheRoomScreen(
                                        daysOfUs = 47,
                                        distanceMiles = 4740,
                                        nextVisitDays = 23,
                                        partnerName = partnerName,
                                        partnerIsAsleep = partnerIsAsleep,
                                        onLightLamp = {
                                            viewModel.sendHeartbeatPoke("Lit the thinking of you lamp ✨")
                                        },
                                        onSendEmote = { emote ->
                                            viewModel.sendHeartbeatPoke("Offered a $emote to $partnerName")
                                        },
                                        onOpenSettings = {
                                            currentDestination = AppDestination.AvatarCreator
                                        },
                                        onTapDoodle = {
                                            selectedTab = 1
                                        },
                                        onTapRecordPlayer = {
                                            currentDestination = AppDestination.QuietCompany
                                        },
                                        onTapTogetherCard = {
                                            selectedTab = 3
                                        }
                                    )
                                    1 -> PlayHubScreen(
                                        partnerName = partnerName,
                                        partnerIsPresent = true,
                                        onOpenLudo = { currentDestination = AppDestination.LudoGame },
                                        onOpenChess = { currentDestination = AppDestination.LudoGame },
                                        onOpenConnectFour = { currentDestination = AppDestination.LudoGame },
                                        onOpen36Questions = { currentDestination = AppDestination.QuestionsDeck },
                                        onOpenWouldYouRather = { currentDestination = AppDestination.QuestionsDeck },
                                        onOpenRoseBudThorn = { currentDestination = AppDestination.QuestionsDeck },
                                        onOpenGuessMyAnswer = { currentDestination = AppDestination.QuestionsDeck }
                                    )
                                    2 -> MailboxScreen(
                                        partnerName = partnerName,
                                        onSendNote = { noteText ->
                                            viewModel.addLoveNote(
                                                title = "Letter for $partnerName",
                                                message = noteText,
                                                unlockCondition = "Instant",
                                                colorTag = 0
                                            )
                                        }
                                    )
                                    3 -> TogetherScreen(
                                        daysUntilVisit = 23,
                                        partnerName = partnerName,
                                        onPlanDate = { dateTitle ->
                                            viewModel.addBucketItem(dateTitle, "Date Night", "Planned together in calendar")
                                        }
                                    )
                                    4 -> MemoriesShelfScreen(
                                        partnerName = partnerName
                                    )
                                }
                            }
                        }
                    }
                }

                AppDestination.WelcomePairing -> {
                    WelcomePairingScreen(
                        inviteCode = "K7R2M9",
                        onProceedToAvatar = {
                            currentDestination = AppDestination.AvatarCreator
                        },
                        onJoinWithCode = {
                            currentDestination = AppDestination.AvatarCreator
                        }
                    )
                }

                AppDestination.AvatarCreator -> {
                    AvatarCreatorScreen(
                        partnerName = partnerName,
                        onBack = { currentDestination = AppDestination.MainTabs },
                        onSaveAvatar = { _, _, _ ->
                            currentDestination = AppDestination.MainTabs
                        }
                    )
                }

                AppDestination.LudoGame -> {
                    LudoGameScreen(
                        partnerName = partnerName,
                        onBack = { currentDestination = AppDestination.MainTabs }
                    )
                }

                AppDestination.QuestionsDeck -> {
                    QuestionsDeckScreen(
                        partnerName = partnerName,
                        onBack = { currentDestination = AppDestination.MainTabs }
                    )
                }

                AppDestination.DecorateRoom -> {
                    DecorateScreen(
                        partnerName = partnerName,
                        onClose = { currentDestination = AppDestination.MainTabs }
                    )
                }

                AppDestination.QuietCompany -> {
                    QuietCompanyScreen(
                        partnerName = partnerName,
                        onBack = { currentDestination = AppDestination.MainTabs }
                    )
                }
            }
        }
    }
}
