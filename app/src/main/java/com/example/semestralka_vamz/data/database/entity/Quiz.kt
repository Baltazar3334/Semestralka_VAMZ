package com.example.semestralka_vamz.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answer1") val answer1: String,
    @ColumnInfo(name = "answer2") val answer2: String,
    @ColumnInfo(name = "answer3") val answer3: String,
    @ColumnInfo(name = "answerC") val correctAnswer: String
)