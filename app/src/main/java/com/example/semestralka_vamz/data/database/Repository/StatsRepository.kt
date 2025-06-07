package com.example.semestralka_vamz.data.database.Repository

import com.example.semestralka_vamz.data.database.UserStatsDao
import com.example.semestralka_vamz.data.database.entity.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class StatsRepository(private val dao: UserStatsDao) {

    val statsFlow: Flow<UserStats?> = dao.getStats()

    suspend fun updateStats(correct: Int, total: Int, isPerfect: Boolean) {
        val current = dao.getStats().firstOrNull() ?: UserStats()
        val updated = current.copy(
            totalQuizzesCompleted = current.totalQuizzesCompleted + 1,
            perfectScores = current.perfectScores + if (isPerfect) 1 else 0,
            totalCorrectAnswers = current.totalCorrectAnswers + correct,
            totalQuestionsAnswered = current.totalQuestionsAnswered + total
        )
        dao.insertStats(updated)
    }
}