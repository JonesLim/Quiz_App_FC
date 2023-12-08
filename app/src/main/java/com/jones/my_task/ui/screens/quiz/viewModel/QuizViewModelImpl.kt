package com.jones.my_task.ui.screens.quiz.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.jones.my_task.data.model.Quiz
import com.jones.my_task.data.repo.QuizRepo
import com.jones.my_task.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
) : BaseViewModel(), QuizViewModel {

    private val _quiz: MutableStateFlow<Quiz> = MutableStateFlow(
        Quiz(
            title = "",
            questions = emptyList(),
            options = emptyList(),
            correctOption = emptyList(),
            createdBy = "",
            second = 0
        )
    )
    val quiz: StateFlow<Quiz> = _quiz

    override fun getQuiz(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { quizRepo.getQuiz(id) }?.let {
                _quiz.value = it
                Log.d("debugging", it.toString())
            }
        }
    }
}