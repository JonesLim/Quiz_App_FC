package com.jones.my_task.ui.screens.home.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.jones.my_task.core.service.AuthService
import com.jones.my_task.data.model.Quiz
import com.jones.my_task.data.repo.QuizRepo
import com.jones.my_task.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService

) : BaseViewModel(), HomeViewModel {

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    override val quizzes: StateFlow<List<Quiz>> get() = _quizzes.asStateFlow()

    init {
        getQuizzes()
    }

    override fun getQuizzes() {
        viewModelScope.launch {
            quizRepo.getQuizzes().collect {
                _quizzes.value = it
                Log.d("debugging", it.toString())
            }
        }
    }


    override fun deleteQuiz(quizId: String) {
        viewModelScope.launch {
            quizRepo.deleteQuiz(quizId)
        }
    }

    override fun updateQuiz(quiz: Quiz) {
        viewModelScope.launch {
            quizRepo.updateQuiz(quiz)

            getQuizzes()
        }
    }

    override fun logout() {
        authService.logout()
    }

    val currentUser = authService.getCurrentUser()
}
