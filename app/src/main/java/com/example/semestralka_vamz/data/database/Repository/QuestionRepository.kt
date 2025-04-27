package com.example.semestralka_vamz.data.database.Repository

import com.example.semestralka_vamz.data.database.QuestionDao
import com.example.semestralka_vamz.data.database.entity.Question

class QuestionRepository(private val questionDao: QuestionDao) {
    suspend fun addQuestion(question: Question) {
        questionDao.insert(question)
    }
}