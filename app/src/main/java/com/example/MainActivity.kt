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
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AppTabBar
import com.example.ui.screens.AvatarCreatorScreen
import com.example.ui.screens.ChessGameScreen
import com.example.ui.screens.ConnectFourGameScreen
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
    data object ChessGame : AppDestination()
    data object ConnectFourGame : AppDestination()
    data class QuestionDeckDest(val deckId: String) : AppDestination()
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

    val hangoutState by viewModel.hangoutState.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()
    val bucketItems by viewModel.bucketItems.collectAsStateWithLifecycle()
    val recentGames by viewModel.recentGames.collectAsStateWithLifecycle()

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
                                    0 -> {
                                        val daysTogether = ((System.currentTimeMillis() - hangoutState.anniversaryEpochMillis) / (24L * 60 * 60 * 1000)).coerceAtLeast(1).toInt()
                                        val daysToVisit = ((hangoutState.nextVisitEpochMillis - System.currentTimeMillis()) / (24L * 60 * 60 * 1000)).coerceAtLeast(0).toInt()

                                        TheRoomScreen(
                                            daysOfUs = daysTogether,
                                            distanceMiles = 4740,
                                            nextVisitDays = daysToVisit,
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
                                    }
                                    1 -> PlayHubScreen(
                                        partnerName = partnerName,
                                        partnerIsPresent = true,
                                        onOpenLudo = { currentDestination = AppDestination.LudoGame },
                                        onOpenChess = { currentDestination = AppDestination.ChessGame },
                                        onOpenConnectFour = { currentDestination = AppDestination.ConnectFourGame },
                                        onOpen36Questions = { currentDestination = AppDestination.QuestionDeckDest("36questions") },
                                        onOpenWouldYouRather = { currentDestination = AppDestination.QuestionDeckDest("wyr") },
                                        onOpenRoseBudThorn = { currentDestination = AppDestination.QuestionDeckDest("rosebudthorn") },
                                        onOpenGuessMyAnswer = { currentDestination = AppDestination.QuestionDeckDest("guess") }
                                    )
                                    2 -> MailboxScreen(
                                        partnerName = partnerName,
                                        notes = allNotes,
                                        onSendNote = { title, body, kind, unlock ->
                                            viewModel.addLoveNote(
                                                title = title,
                                                message = body,
                                                unlockCondition = unlock,
                                                colorTag = if (kind == "Postcard") 1 else 0
                                            )
                                        },
                                        onOpenNote = { id ->
                                            val note = allNotes.find { it.id == id }
                                            if (note != null) viewModel.openLoveNote(note)
                                        },
                                        onDeleteNote = { id ->
                                            viewModel.deleteLoveNote(id)
                                        }
                                    )
                                    3 -> {
                                        val daysToVisit = ((hangoutState.nextVisitEpochMillis - System.currentTimeMillis()) / (24L * 60 * 60 * 1000)).coerceAtLeast(0).toInt()

                                        TogetherScreen(
                                            daysUntilVisit = daysToVisit,
                                            partnerName = partnerName,
                                            eventsList = bucketItems,
                                            onPlanDate = { title, category, dateText ->
                                                viewModel.addBucketItem(title, category, dateText)
                                            },
                                            onToggleEventCompleted = { item ->
                                                viewModel.toggleBucketItem(item)
                                            },
                                            onDeleteEvent = { id ->
                                                viewModel.deleteBucketItem(id)
                                            },
                                            onChangeVisitDays = { days ->
                                                viewModel.updateNextVisitDate(
                                                    System.currentTimeMillis() + (days.toLong() * 24 * 60 * 60 * 1000),
                                                    "Reunion in the same room ✨"
                                                )
                                            }
                                        )
                                    }
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
                        onBack = { currentDestination = AppDestination.MainTabs },
                        onGameFinished = { winPlayer, score ->
                            viewModel.recordGame(
                                gameType = "Ludo",
                                kolaScore = if (winPlayer == "Kola") 4 else 2,
                                joyScore = if (winPlayer == "Joy") 4 else 2,
                                winner = winPlayer,
                                summary = "$winPlayer won the Ludo match ($score) in the attic room."
                            )
                        }
                    )
                }

                AppDestination.ChessGame -> {
                    ChessGameScreen(
                        partnerName = partnerName,
                        onBack = { currentDestination = AppDestination.MainTabs },
                        onGameFinished = { winPlayer, moves ->
                            viewModel.recordGame(
                                gameType = "Chess",
                                kolaScore = if (winPlayer == "Kola") 1 else 0,
                                joyScore = if (winPlayer == "Joy") 1 else 0,
                                winner = winPlayer,
                                summary = "$winPlayer won Chess in $moves moves."
                            )
                        }
                    )
                }

                AppDestination.ConnectFourGame -> {
                    ConnectFourGameScreen(
                        partnerName = partnerName,
                        onBack = { currentDestination = AppDestination.MainTabs },
                        onGameFinished = { winPlayer, score ->
                            viewModel.recordGame(
                                gameType = "Connect Four",
                                kolaScore = if (winPlayer == "Kola") 1 else 0,
                                joyScore = if (winPlayer == "Joy") 1 else 0,
                                winner = winPlayer,
                                summary = "$winPlayer won Connect Four ($score)."
                            )
                        }
                    )
                }

                is AppDestination.QuestionDeckDest -> {
                    QuestionsDeckScreen(
                        partnerName = partnerName,
                        initialDeckId = dest.deckId,
                        onBack = { currentDestination = AppDestination.MainTabs },
                        onSaveToMemories = { question, answer ->
                            viewModel.addLoveNote(
                                title = "Shared Memory: Question",
                                message = "Q: $question\nAnswer: $answer",
                                unlockCondition = "Instant",
                                colorTag = 2
                            )
                        }
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
