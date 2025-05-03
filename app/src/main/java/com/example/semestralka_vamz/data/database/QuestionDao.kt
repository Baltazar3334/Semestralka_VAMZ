package com.example.semestralka_vamz.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.semestralka_vamz.data.database.entity.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Insert
    suspend fun insert(question: Question)

    @Query("SELECT * FROM question_table")
    fun getAllQuestions(): Flow<List<Question>>

}