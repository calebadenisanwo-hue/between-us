package com.example.data.repository

import com.example.data.db.AppDao
import com.example.data.model.BucketItem
import com.example.data.model.GameRecord
import com.example.data.model.HangoutState
import com.example.data.model.LoveNote
import com.example.data.model.QuestionAnswer
import com.example.data.model.SharedDoodle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class BetweenUsRepository(private val appDao: AppDao) {

    val allQuestions: Flow<List<QuestionAnswer>> = appDao.getAllQuestions()
    val dailyQuestion: Flow<QuestionAnswer?> = appDao.getDailyQuestion()
    val allNotes: Flow<List<LoveNote>> = appDao.getAllNotes()
    val allBucketItems: Flow<List<BucketItem>> = appDao.getAllBucketItems()
    val hangoutState: Flow<HangoutState?> = appDao.getHangoutState()
    val recentGames: Flow<List<GameRecord>> = appDao.getRecentGames()
    val allDoodles: Flow<List<SharedDoodle>> = appDao.getAllDoodles()

    suspend fun initializeDefaultsIfNeeded() {
        val existingState = appDao.getHangoutState().firstOrNull()
        if (existingState == null) {
            appDao.setHangoutState(HangoutState())
        }

        val existingQuestions = appDao.getAllQuestions().firstOrNull()
        if (existingQuestions.isNullOrEmpty()) {
            val defaults = getInitialQuestionBank()
            appDao.insertQuestions(defaults)
        }

        val existingNotes = appDao.getAllNotes().firstOrNull()
        if (existingNotes.isNullOrEmpty()) {
            val defaultNotes = listOf(
                LoveNote(
                    sender = "Kola",
                    recipient = "Joy",
                    title = "Before you sleep tonight",
                    message = "Every mile between London and Lagos is just proof of how real and powerful our love is. You are my home, Joy. Always. Sleep well my queen ❤️",
                    unlockCondition = "Instant",
                    isOpened = true,
                    colorTag = 0
                ),
                LoveNote(
                    sender = "Joy",
                    recipient = "Kola",
                    title = "Open when you have a rough day",
                    message = "Take a deep breath, Kola. Look at our photos, remember our laughs, and know I am cheering for you from across the ocean. You've got this, handsome! 🌟",
                    unlockCondition = "Open when sad",
                    isOpened = false,
                    colorTag = 1
                ),
                LoveNote(
                    sender = "Kola",
                    recipient = "Joy",
                    title = "The London airport countdown",
                    message = "I already have the biggest bear hug planned the exact second you walk out through terminal arrivals. Counting down every single second! ✈️🫂",
                    unlockCondition = "Open when missing me",
                    isOpened = false,
                    colorTag = 2
                )
            )
            for (note in defaultNotes) {
                appDao.insertNote(note)
            }
        }

        val existingBucket = appDao.getAllBucketItems().firstOrNull()
        if (existingBucket.isNullOrEmpty()) {
            val defaultBucket = listOf(
                BucketItem(
                    title = "Cook authentic Jollof Rice & Plantains together in one kitchen",
                    category = "Food & Dining",
                    addedBy = "Joy",
                    isCompleted = false,
                    notes = "Kola claims his recipe will win the taste test!"
                ),
                BucketItem(
                    title = "Sunset picnic at Greenwich Park overlooking London skyline",
                    category = "Cozy Date",
                    addedBy = "Kola",
                    isCompleted = false,
                    notes = "Warm tea, blanket, and our favorite songs."
                ),
                BucketItem(
                    title = "Beach walk at Elegushi Beach at twilight listening to the waves",
                    category = "Travel & Adventure",
                    addedBy = "Joy",
                    isCompleted = false
                ),
                BucketItem(
                    title = "Late night drive with the windows down singing Afrobeats",
                    category = "Cozy Date",
                    addedBy = "Kola",
                    isCompleted = true,
                    completedDate = "Memorable first trip!"
                ),
                BucketItem(
                    title = "Build a pillow & fairy light fort to watch an entire movie series",
                    category = "Cozy Date",
                    addedBy = "Joy",
                    isCompleted = false
                )
            )
            for (item in defaultBucket) {
                appDao.insertBucketItem(item)
            }
        }
    }

    suspend fun saveAnswer(questionId: Int, profile: String, answer: String) {
        val q = appDao.getQuestionById(questionId) ?: return
        val updated = if (profile.equals("Kola", ignoreCase = true)) {
            q.copy(kolaAnswer = answer, kolaAnsweredAt = System.currentTimeMillis())
        } else {
            q.copy(joyAnswer = answer, joyAnsweredAt = System.currentTimeMillis())
        }
        appDao.updateQuestion(updated)
    }

    suspend fun toggleQuestionFavorite(id: Int, currentFav: Boolean) {
        appDao.toggleFavorite(id, !currentFav)
    }

    suspend fun updateHangoutState(state: HangoutState) {
        appDao.setHangoutState(state)
    }

    suspend fun addNote(note: LoveNote) {
        appDao.insertNote(note)
    }

    suspend fun updateNote(note: LoveNote) {
        appDao.updateNote(note)
    }

    suspend fun deleteNote(id: Int) {
        appDao.deleteNote(id)
    }

    suspend fun addBucketItem(item: BucketItem) {
        appDao.insertBucketItem(item)
    }

    suspend fun toggleBucketItem(item: BucketItem) {
        val completed = !item.isCompleted
        val date = if (completed) "Completed together 🎉" else null
        appDao.updateBucketItem(item.copy(isCompleted = completed, completedDate = date))
    }

    suspend fun deleteBucketItem(id: Int) {
        appDao.deleteBucketItem(id)
    }

    suspend fun saveGameRecord(record: GameRecord) {
        appDao.insertGameRecord(record)
    }

    suspend fun saveDoodle(doodle: SharedDoodle) {
        appDao.insertDoodle(doodle)
    }

    suspend fun deleteDoodle(id: Int) {
        appDao.deleteDoodle(id)
    }

    private fun getInitialQuestionBank(): List<QuestionAnswer> {
        return listOf(
            QuestionAnswer(
                questionId = "q_daily_1",
                questionText = "What was the very specific moment you realized you had real feelings for me?",
                category = "Romance",
                kolaAnswer = "When you laughed so hard on our video call that you dropped your mug, and instead of being embarrassed, you just beamed at me. My heart melted.",
                joyAnswer = "When you stayed on audio with me while I was terrified of thunder, speaking in that soft calm voice until I drifted to sleep.",
                kolaAnsweredAt = System.currentTimeMillis() - 86400000,
                joyAnsweredAt = System.currentTimeMillis() - 72000000,
                isFavorite = true,
                isDaily = true
            ),
            QuestionAnswer(
                questionId = "q_deep_1",
                questionText = "When distance feels heavy, what is the single thought that instantly makes you feel close to me?",
                category = "Deep",
                kolaAnswer = "Looking at the moon at night and reminding myself that we are under the very same sky.",
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_silly_1",
                questionText = "If we were in a zombie apocalypse together, who dies first and what is our survival strategy?",
                category = "Silly",
                kolaAnswer = "Joy dies first because she stops to pet an infected kitten! I will then heroically avenge her with a cricket bat.",
                joyAnswer = "Excuse you Kola! I would be the mastermind building the barricades while you get lost looking for snack bars!"
            ),
            QuestionAnswer(
                questionId = "q_future_1",
                questionText = "Describe our dream Sunday morning once we close the distance and live under one roof.",
                category = "Future",
                kolaAnswer = null,
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_memories_1",
                questionText = "What is the funniest mishap or inside joke we've shared so far?",
                category = "Memories",
                kolaAnswer = null,
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_deep_2",
                questionText = "What is a small habit of mine that you secretly adore?",
                category = "Deep",
                kolaAnswer = null,
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_romance_2",
                questionText = "What song lyrics remind you most purely of how you feel about us?",
                category = "Romance",
                kolaAnswer = null,
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_silly_2",
                questionText = "If you could swap bodies with me for 24 hours, what is the first thing you would do?",
                category = "Silly",
                kolaAnswer = null,
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_future_2",
                questionText = "What is the very first country outside of Nigeria and the UK we should explore together?",
                category = "Future",
                kolaAnswer = null,
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_deep_3",
                questionText = "How has being in this long-distance relationship changed the way you view love and commitment?",
                category = "Deep",
                kolaAnswer = null,
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_romance_3",
                questionText = "What are three adjectives you would use to describe the feeling of my embrace?",
                category = "Romance",
                kolaAnswer = null,
                joyAnswer = null
            ),
            QuestionAnswer(
                questionId = "q_silly_3",
                questionText = "Rate my texting emoji habits from 1 to 10 and roast my most overused emoji!",
                category = "Silly",
                kolaAnswer = null,
                joyAnswer = null
            )
        )
    }
}
