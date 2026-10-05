package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.BucketItem
import com.example.data.model.GameRecord
import com.example.data.model.HangoutState
import com.example.data.model.LoveNote
import com.example.data.model.QuestionAnswer
import com.example.data.model.SharedDoodle
import com.example.data.repository.BetweenUsRepository
import com.example.ui.audio.AmbientSoundManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BetweenUsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BetweenUsRepository
    val soundManager: AmbientSoundManager = AmbientSoundManager(application)

    // Current active perspective: "Kola" or "Joy"
    private val _currentProfile = MutableStateFlow("Kola")
    val currentProfile: StateFlow<String> = _currentProfile.asStateFlow()

    // Heartbeat pulse visual effect counter / trigger
    private val _heartbeatAnimTrigger = MutableStateFlow(0L)
    val heartbeatAnimTrigger: StateFlow<Long> = _heartbeatAnimTrigger.asStateFlow()

    val hangoutState: StateFlow<HangoutState>
    val allQuestions: StateFlow<List<QuestionAnswer>>
    val allNotes: StateFlow<List<LoveNote>>
    val bucketItems: StateFlow<List<BucketItem>>
    val recentGames: StateFlow<List<GameRecord>>
    val doodles: StateFlow<List<SharedDoodle>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = BetweenUsRepository(database.appDao())

        hangoutState = repository.hangoutState
            .map { it ?: HangoutState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HangoutState()
            )

        allQuestions = repository.allQuestions
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        allNotes = repository.allNotes
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        bucketItems = repository.allBucketItems
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        recentGames = repository.recentGames
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        doodles = repository.allDoodles
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
        }
    }

    fun switchProfile(profile: String) {
        _currentProfile.value = profile
        val current = hangoutState.value
        viewModelScope.launch {
            repository.updateHangoutState(current.copy(activeProfile = profile))
        }
    }

    fun updateMood(mood: String, activity: String) {
        val current = hangoutState.value
        val isKola = _currentProfile.value == "Kola"
        val updated = if (isKola) {
            current.copy(kolaMood = mood, kolaActivity = activity)
        } else {
            current.copy(joyMood = mood, joyActivity = activity)
        }
        viewModelScope.launch {
            repository.updateHangoutState(updated)
        }
    }

    fun sendHeartbeatPoke(message: String? = null) {
        val current = hangoutState.value
        val sender = _currentProfile.value
        val recipient = if (sender == "Kola") "Joy" else "Kola"
        val customMsg = message ?: "$sender sent a lingering heartbeat & warm butterflies! ✨"

        soundManager.triggerHeartbeatHaptic()
        _heartbeatAnimTrigger.value = System.currentTimeMillis()

        viewModelScope.launch {
            repository.updateHangoutState(
                current.copy(
                    pokeCount = current.pokeCount + 1,
                    lastPokedBy = sender,
                    lastPokedMessage = customMsg
                )
            )
        }
    }

    fun waterPlant() {
        val current = hangoutState.value
        val newWater = (current.plantWaterLevel + 25).coerceAtMost(100)
        val newLove = current.plantLoveLevel + 15
        val newStage = when {
            newLove >= 300 -> 4 // Evergreen Love Tree
            newLove >= 200 -> 3 // Blooming Jasmine
            newLove >= 100 -> 2 // Young Bonsai
            else -> 1 // Sprout
        }
        soundManager.triggerHeartbeatHaptic()
        viewModelScope.launch {
            repository.updateHangoutState(
                current.copy(
                    plantWaterLevel = newWater,
                    plantLoveLevel = newLove,
                    plantStage = newStage,
                    lastWateredEpoch = System.currentTimeMillis()
                )
            )
        }
    }

    fun givePlantSunlight() {
        val current = hangoutState.value
        val newSun = (current.plantSunLevel + 20).coerceAtMost(100)
        val newLove = current.plantLoveLevel + 10
        val newStage = when {
            newLove >= 300 -> 4
            newLove >= 200 -> 3
            newLove >= 100 -> 2
            else -> 1
        }
        viewModelScope.launch {
            repository.updateHangoutState(
                current.copy(
                    plantSunLevel = newSun,
                    plantLoveLevel = newLove,
                    plantStage = newStage
                )
            )
        }
    }

    fun toggleAmbientSound(mode: String) {
        val current = hangoutState.value
        val newMode = if (current.ambientSound == mode) "None" else mode
        soundManager.startSound(newMode)
        viewModelScope.launch {
            repository.updateHangoutState(current.copy(ambientSound = newMode))
        }
    }

    fun saveAnswer(questionId: Int, answer: String) {
        val profile = _currentProfile.value
        viewModelScope.launch {
            repository.saveAnswer(questionId, profile, answer)
        }
    }

    fun toggleQuestionFavorite(question: QuestionAnswer) {
        viewModelScope.launch {
            repository.toggleQuestionFavorite(question.id, question.isFavorite)
        }
    }

    fun addLoveNote(title: String, message: String, unlockCondition: String, colorTag: Int) {
        val sender = _currentProfile.value
        val recipient = if (sender == "Kola") "Joy" else "Kola"
        val note = LoveNote(
            sender = sender,
            recipient = recipient,
            title = title,
            message = message,
            unlockCondition = unlockCondition,
            isOpened = (unlockCondition == "Instant"),
            colorTag = colorTag
        )
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

    fun openLoveNote(note: LoveNote) {
        if (!note.isOpened) {
            viewModelScope.launch {
                repository.updateNote(note.copy(isOpened = true, openedAt = System.currentTimeMillis()))
            }
        }
    }

    fun deleteLoveNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun addBucketItem(title: String, category: String, notes: String?) {
        val sender = _currentProfile.value
        val item = BucketItem(
            title = title,
            category = category,
            addedBy = sender,
            notes = notes
        )
        viewModelScope.launch {
            repository.addBucketItem(item)
        }
    }

    fun toggleBucketItem(item: BucketItem) {
        viewModelScope.launch {
            repository.toggleBucketItem(item)
        }
    }

    fun deleteBucketItem(id: Int) {
        viewModelScope.launch {
            repository.deleteBucketItem(id)
        }
    }

    fun updateNextVisitDate(epochMillis: Long, label: String = "Reunion in the same room ✨") {
        val current = hangoutState.value
        viewModelScope.launch {
            repository.updateHangoutState(current.copy(nextVisitEpochMillis = epochMillis, nextVisitLabel = label))
        }
    }

    fun recordGame(gameType: String, kolaScore: Int, joyScore: Int, winner: String?, summary: String) {
        viewModelScope.launch {
            repository.saveGameRecord(
                GameRecord(
                    gameType = gameType,
                    kolaScore = kolaScore,
                    joyScore = joyScore,
                    winner = winner,
                    summary = summary
                )
            )
        }
    }

    fun saveDoodle(title: String, strokeJson: String) {
        val sender = _currentProfile.value
        viewModelScope.launch {
            repository.saveDoodle(
                SharedDoodle(
                    drawnBy = sender,
                    title = title,
                    strokePathsJson = strokeJson
                )
            )
        }
    }

    fun deleteDoodle(id: Int) {
        viewModelScope.launch {
            repository.deleteDoodle(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.stopSound()
    }
}
