package com.example.semestralka_vamz.data.database.Repository

import com.example.semestralka_vamz.data.database.QuizDao
import com.example.semestralka_vamz.data.database.entity.Quiz
import kotlinx.coroutines.flow.Flow

class QuizRepository(private val quizDao: QuizDao) {
    val allQuizzes: Flow<List<Quiz>> = quizDao.getAllQuizzes()

    suspend fun addQuizReturningId(quiz: Quiz): Long {
        return quizDao.insertAndReturnId(quiz)
    }

    suspend fun updateQuiz(quiz: Quiz) {
        quizDao.update(quiz)
    }

    suspend fun deleteQuiz(quiz: Quiz) {
        quizDao.delete(quiz)
    }

    fun getQuizzes(): Flow<List<Quiz>> {
        return allQuizzes
    }



}