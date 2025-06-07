package com.example.semestralka_vamz.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserStats(
    @PrimaryKey val id: Int = 0,
    val totalQuizzesCompleted: Int = 0,
    val perfectScores: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalQuestionsAnswered: Int = 0,
    val lastQuizId: Long? = null
)