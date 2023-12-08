package com.jones.my_task.data.repo

import com.jones.my_task.data.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepo {
    suspend fun getQuizzes(): Flow<List<Quiz>>
    suspend fun addQuiz(quiz: Quiz)
    suspend fun getQuiz(id: String): Quiz?
    suspend fun deleteQuiz(id: String)
    suspend fun updateQuiz(quiz: Quiz)
}
