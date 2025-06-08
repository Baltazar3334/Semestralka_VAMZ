package com.example.semestralka_vamz.data.database.Repository

import com.example.semestralka_vamz.data.database.QuizDao
import com.example.semestralka_vamz.data.database.entity.Quiz
import kotlinx.coroutines.flow.Flow

class QuizRepository(private val quizDao: QuizDao) {
    val allQuizzes: Flow<List<Quiz>> = quizDao.getAllQuizzes()

    suspend fun addQuizReturningId(quiz: Quiz): Long {
        return quizDao.insertAndReturnId(quiz)
    }

    fun getQuizzes(): Flow<List<Quiz>> {
        return allQuizzes
    }

    fun deleteQuizById(id: Long) {
        quizDao.deleteQuizById(id)
    }

}