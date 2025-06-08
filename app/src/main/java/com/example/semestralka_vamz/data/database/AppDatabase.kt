package com.example.semestralka_vamz.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.semestralka_vamz.data.database.entity.Question
import com.example.semestralka_vamz.data.database.entity.Quiz
import com.example.semestralka_vamz.data.database.entity.UserStats

@Database(entities = [Quiz::class, Question::class, UserStats::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun questionDao(): QuestionDao
    abstract fun userStatsDao(): UserStatsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quiz_database"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

