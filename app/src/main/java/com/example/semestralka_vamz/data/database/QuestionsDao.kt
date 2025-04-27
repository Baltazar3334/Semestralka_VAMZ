package com.example.semestralka_vamz.data.database

import androidx.room.Dao
import androidx.room.Insert
import com.example.semestralka_vamz.data.database.entity.Question

@Dao
interface QuestionDao {
    @Insert
    suspend fun insert(question: Question)

}