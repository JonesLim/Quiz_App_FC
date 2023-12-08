package com.jones.my_task.ui.screens.addQuiz.viewModel

import com.jones.my_task.data.model.Quiz
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AddQuizViewModel {
    val done: SharedFlow<Unit>
    fun addQuiz(quiz: Quiz)
    fun readCSV(lines: List<String>)
    val questions: StateFlow<List<String>>
    val options: StateFlow<List<String>>
    val correctOptions: StateFlow<List<Int>>

}