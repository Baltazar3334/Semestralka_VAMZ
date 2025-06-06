package com.example.semestralka_vamz.data.database.Repository

import com.example.semestralka_vamz.data.database.QuestionDao
import com.example.semestralka_vamz.data.database.entity.Question
import kotlinx.coroutines.flow.Flow

class QuestionRepository(private val questionDao: QuestionDao) {
    val allQuestions: Flow<List<Question>> = questionDao.getAllQuestions()

    fun getQuestions(): Flow<List<Question>> {
        return allQuestions
    }



    suspend fun addQuestion(question: Question) {
        questionDao.insert(question)
    }


}