package com.example.semestralka_vamz.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answerIC") val answerIC: String,
    @ColumnInfo(name = "answerC") val AnswerC: String,
    @ColumnInfo(name = "timeLimit") val timeLimit: Int,
    @ColumnInfo(name = "timeLimitOn") val timeLimitOn: Boolean


)