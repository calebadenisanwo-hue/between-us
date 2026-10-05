package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BucketItem
import com.example.data.model.GameRecord
import com.example.data.model.HangoutState
import com.example.data.model.LoveNote
import com.example.data.model.QuestionAnswer
import com.example.data.model.SharedDoodle
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // Questions
    @Query("SELECT * FROM question_answers ORDER BY id ASC")
    fun getAllQuestions(): Flow<List<QuestionAnswer>>

    @Query("SELECT * FROM question_answers WHERE isDaily = 1 LIMIT 1")
    fun getDailyQuestion(): Flow<QuestionAnswer?>

    @Query("SELECT * FROM question_answers WHERE id = :id")
    suspend fun getQuestionById(id: Int): QuestionAnswer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionAnswer>)

    @Update
    suspend fun updateQuestion(question: QuestionAnswer)

    @Query("UPDATE question_answers SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: Int, isFav: Boolean)

    // Love Notes
    @Query("SELECT * FROM love_notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<LoveNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: LoveNote)

    @Update
    suspend fun updateNote(note: LoveNote)

    @Query("DELETE FROM love_notes WHERE id = :id")
    suspend fun deleteNote(id: Int)

    // Bucket List
    @Query("SELECT * FROM bucket_items ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllBucketItems(): Flow<List<BucketItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBucketItem(item: BucketItem)

    @Update
    suspend fun updateBucketItem(item: BucketItem)

    @Query("DELETE FROM bucket_items WHERE id = :id")
    suspend fun deleteBucketItem(id: Int)

    // Hangout State
    @Query("SELECT * FROM hangout_state WHERE id = 1")
    fun getHangoutState(): Flow<HangoutState?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setHangoutState(state: HangoutState)

    // Game Records
    @Query("SELECT * FROM game_records ORDER BY playedAt DESC LIMIT 20")
    fun getRecentGames(): Flow<List<GameRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameRecord(record: GameRecord)

    // Doodles
    @Query("SELECT * FROM shared_doodles ORDER BY createdAt DESC")
    fun getAllDoodles(): Flow<List<SharedDoodle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoodle(doodle: SharedDoodle)

    @Query("DELETE FROM shared_doodles WHERE id = :id")
    suspend fun deleteDoodle(id: Int)
}
