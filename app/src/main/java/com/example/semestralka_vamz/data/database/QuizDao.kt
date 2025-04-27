package com.example.semestralka_vamz.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.semestralka_vamz.data.database.entity.Quiz

@Dao
interface QuizDao {

    @Insert
    suspend fun insert(quiz: Quiz)

    @Update
    suspend fun update(quiz: Quiz)

    @Delete
    suspend fun delete(quiz: Quiz)

    @Query("SELECT * FROM quiz_table")
    suspend fun getAllQuizzes(): List<Quiz>
}