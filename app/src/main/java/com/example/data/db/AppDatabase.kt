package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BucketItem
import com.example.data.model.GameRecord
import com.example.data.model.HangoutState
import com.example.data.model.LoveNote
import com.example.data.model.QuestionAnswer
import com.example.data.model.SharedDoodle

@Database(
    entities = [
        QuestionAnswer::class,
        LoveNote::class,
        BucketItem::class,
        HangoutState::class,
        GameRecord::class,
        SharedDoodle::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "between_us_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
