package com.example.semestralka_vamz.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.semestralka_vamz.data.database.entity.Quiz

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quiz: Quiz)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(quiz: Quiz)

    @Delete
    suspend fun delete(quiz: Quiz)

    @Insert
    suspend fun insertAndReturnId(quiz: Quiz): Long


}