package com.example.semestralka_vamz.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.semestralka_vamz.data.database.entity.Quiz
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quiz: Quiz)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(quiz: Quiz)

    @Query("DELETE FROM quiz_table WHERE id = :quizId")
    fun deleteQuizById(quizId: Long)

    @Delete
    suspend fun delete(quiz: Quiz)

    @Insert
    suspend fun insertAndReturnId(quiz: Quiz): Long

    @Query("SELECT * FROM quiz_table")
    fun getAllQuizzes(): Flow<List<Quiz>>

    @Query("UPDATE quiz_table SET favourite = :isFavourite WHERE id = :questionId")
    suspend fun updateFavourite(questionId: Long, isFavourite: Boolean)

    @Query("SELECT favourite FROM quiz_table WHERE id = :questionId")
    suspend fun isFavourite(questionId: Long): Boolean

}