package com.jones.my_task.ui.screens.home.viewModel

import com.jones.my_task.data.model.Quiz
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    val quizzes: StateFlow<List<Quiz>>

    fun getQuizzes()
    fun deleteQuiz(quizId: String)
    fun updateQuiz(quiz: Quiz)
    fun logout()
}