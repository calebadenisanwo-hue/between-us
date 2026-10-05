package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_answers")
data class QuestionAnswer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questionId: String,
    val questionText: String,
    val category: String, // "Deep", "Silly", "Romance", "Future", "Memories"
    val kolaAnswer: String? = null,
    val joyAnswer: String? = null,
    val kolaAnsweredAt: Long? = null,
    val joyAnsweredAt: Long? = null,
    val isFavorite: Boolean = false,
    val isDaily: Boolean = false
)

@Entity(tableName = "love_notes")
data class LoveNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sender: String, // "Kola" or "Joy"
    val recipient: String, // "Joy" or "Kola"
    val title: String,
    val message: String,
    val unlockCondition: String = "Instant", // "Instant", "Open when sad", "Open on date night", "Open when missing me"
    val isOpened: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val openedAt: Long? = null,
    val colorTag: Int = 0 // 0-5 palette
)

@Entity(tableName = "bucket_items")
data class BucketItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String, // "Cozy Date", "Food & Dining", "Travel & Adventure", "Milestone"
    val addedBy: String,
    val isCompleted: Boolean = false,
    val targetDate: String? = null,
    val completedDate: String? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "hangout_state")
data class HangoutState(
    @PrimaryKey
    val id: Int = 1,
    val activeProfile: String = "Kola", // "Kola" or "Joy"
    val kolaMood: String = "Missing you 💭",
    val joyMood: String = "Cozy & smiling ☕",
    val kolaActivity: String = "Listening to our playlist 🎵",
    val joyActivity: String = "Curled up with tea 🫖",
    val kolaLocation: String = "London, UK",
    val joyLocation: String = "Lagos, Nigeria",
    val anniversaryEpochMillis: Long = System.currentTimeMillis() - (180L * 24 * 60 * 60 * 1000), // ~6 months ago
    val nextVisitEpochMillis: Long = System.currentTimeMillis() + (24L * 24 * 60 * 60 * 1000), // 24 days away
    val nextVisitLabel: String = "London Airport Reunion ✈️",
    val plantWaterLevel: Int = 75,
    val plantSunLevel: Int = 80,
    val plantLoveLevel: Int = 120, // growth xp
    val plantStage: Int = 2, // 1=Sprout, 2=Young Bonsai, 3=Blooming Jasmine, 4=Evergreen Love Tree
    val lastWateredEpoch: Long = System.currentTimeMillis(),
    val pokeCount: Int = 24,
    val lastPokedBy: String? = "Joy",
    val lastPokedMessage: String? = "Sent a warm hug and butterflies! ✨",
    val ambientSound: String = "None" // "None", "Rain", "Campfire", "Lofi"
)

@Entity(tableName = "game_records")
data class GameRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gameType: String, // "TicTacToe", "WouldYouRather", "Trivia", "TwentyQuestions"
    val kolaScore: Int,
    val joyScore: Int,
    val winner: String?,
    val playedAt: Long = System.currentTimeMillis(),
    val summary: String
)

@Entity(tableName = "shared_doodles")
data class SharedDoodle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val drawnBy: String,
    val title: String,
    val strokePathsJson: String, // JSON string of lines
    val createdAt: Long = System.currentTimeMillis()
)
