package com.example.semestralka_vamz.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
//entita Otazka
@Entity(
    tableName = "question_table",
    foreignKeys = [
        ForeignKey(
            entity = Quiz::class,
            parentColumns = ["id"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["quizId"])]
)
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "quizId") val quizId: Long,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "correctAnswer") val correctAnswer: String,
    @ColumnInfo(name = "answer1") val answer1: String?,
    @ColumnInfo(name = "answer2") val answer2: String?,
    @ColumnInfo(name = "answer3") val answer3: String?,
)

