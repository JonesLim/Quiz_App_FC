package com.jones.my_task.ui.screens.quiz

import QuestionAdapter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.jones.my_task.databinding.FragmentQuizBinding
import com.jones.my_task.ui.screens.base.BaseFragment
import com.jones.my_task.ui.screens.quiz.viewModel.QuizViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class QuizFragment : BaseFragment<FragmentQuizBinding>() {


    override val viewModel: QuizViewModelImpl by viewModels()

    private val args: QuizFragmentArgs by navArgs()

    val adapter = QuestionAdapter(mutableMapOf(), emptyList(), emptyList(), emptyList())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuiz(args.quizId)
    }


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.btnDone.setOnClickListener {
            if (adapter.isAllQuestionsAnswered()) {
                showResultLayout(adapter.calculateScore())
            } else {
                showToast("Please answer all questions before clicking Done.")
            }
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }


    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                binding.tbTitle.title = quiz.title

                adapter.setQuestionsAndOptions(questions = quiz.questions, options = quiz.options)

                adapter.setCorrectAnswers(quiz.correctOption.map { it - 1 })


                if (quiz.questions.isNotEmpty() && quiz.options.isNotEmpty()) {
                    val timer = object : CountDownTimer(quiz.second * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val minutes = (millisUntilFinished / 1000) / 60
                            val seconds = (millisUntilFinished / 1000) % 60

                            val formattedTime =
                                String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

                            binding.tvTime.text = formattedTime
                        }

                        override fun onFinish() {
                            val score = adapter.calculateScore()
                            showResultLayout(score)
                        }

                    }
                    timer.start()
                }
            }
        }
    }

    private fun showResultLayout(score: Int) {
        binding.rlQuiz.visibility = View.GONE
        binding.llResult.visibility = View.VISIBLE

        binding.tvTitle.text = "Quiz Title: ${viewModel.quiz.value.title}"
        binding.tvScore.text = "Your Score: $score/${adapter.getCorrectAnswers().size}"

        binding.btnHome.setOnClickListener {
            navController.popBackStack()
        }
    }


    private fun setupRecyclerView() {
        binding.rvQuiz.adapter = adapter
        binding.rvQuiz.layoutManager = LinearLayoutManager(requireContext())

    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}