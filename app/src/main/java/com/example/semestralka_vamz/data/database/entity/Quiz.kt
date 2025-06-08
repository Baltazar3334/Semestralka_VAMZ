package com.example.semestralka_vamz.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//entita Kvíz
@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "timeLimit") val timeLimit: Int,
    @ColumnInfo(name = "timeLimitOn") val timeLimitOn: Boolean,
    @ColumnInfo(name = "favourite") val favourite: Boolean,
    @ColumnInfo(name = "imageUri") val imageUri: String? = null

)