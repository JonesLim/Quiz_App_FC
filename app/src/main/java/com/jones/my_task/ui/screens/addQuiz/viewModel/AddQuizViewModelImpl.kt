package com.jones.my_task.ui.screens.addQuiz.viewModel

import androidx.lifecycle.viewModelScope
import com.jones.my_task.core.service.AuthService
import com.jones.my_task.data.model.Quiz
import com.jones.my_task.data.repo.QuizRepo
import com.jones.my_task.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuizViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService
) : BaseViewModel(), AddQuizViewModel {
    private val _done = MutableSharedFlow<Unit>()
    override val done: SharedFlow<Unit> = _done


    private val _questions = MutableStateFlow<List<String>>(emptyList())
    override val questions: StateFlow<List<String>> = _questions.asStateFlow()


    private val _options = MutableStateFlow<List<String>>(emptyList())
    override val options: StateFlow<List<String>> = _options.asStateFlow()


    private val _correctOptions = MutableStateFlow<List<Int>>(emptyList())
    override val correctOptions: StateFlow<List<Int>> = _correctOptions.asStateFlow()

    override fun addQuiz(quiz: Quiz) {
        val q = quiz.copy(createdBy = authService.getCurrentUser()?.uid ?: "")
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                quizRepo.addQuiz(
                    q
                )
            }
            _done.emit(Unit)
        }
    }

    override fun readCSV(lines: List<String>) {
        val questions = mutableListOf<String>()
        val options = mutableListOf<String>()
        val correctOptions = mutableListOf<Int>()

        lines.forEach {
            val temp = it.split(",")
            questions.add(temp[0])
            options.add(temp[1])
            options.add(temp[2])
            options.add(temp[3])
            options.add(temp[4])
            correctOptions.add(temp[5].toString().toInt())
        }
        _questions.value = questions
        _options.value = options
        _correctOptions.value = correctOptions
    }
}