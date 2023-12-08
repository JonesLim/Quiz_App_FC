package com.jones.my_task.ui.screens.addQuiz

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jones.my_task.data.model.Quiz
import com.jones.my_task.databinding.FragmentAddQuizBinding
import com.jones.my_task.ui.screens.addQuiz.viewModel.AddQuizViewModelImpl
import com.jones.my_task.ui.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@AndroidEntryPoint
class AddQuizFragment : BaseFragment<FragmentAddQuizBinding>() {


    override val viewModel: AddQuizViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.run {
            btnAddQuestion.setOnClickListener {
                val title = editTextTitle.text.toString()
                val timeLimit = editTextTimeLimit.text.toString()

                if (isValidTitle(title) && isValidTimeLimit(timeLimit) && viewModel.questions.value.isNotEmpty()) {
                    viewModel.addQuiz(
                        Quiz(
                            title = title,
                            questions = viewModel.questions.value,
                            createdBy = "",
                            options = viewModel.options.value,
                            correctOption = viewModel.correctOptions.value,
                            second = timeLimit.toLong()
                        )
                    )
                } else {
                    showToast("Invalid input. Please check title, timer, and CSV file.")
                }
            }

            btnImportQuiz.setOnClickListener {
                getContent.launch("text/*")
            }

            btnCancel.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    private fun isValidTitle(title: String): Boolean {
        return title.isNotBlank()
    }

    private fun isValidTimeLimit(timeLimit: String): Boolean {
        return timeLimit.isNotBlank() && timeLimit.toLongOrNull() != null && timeLimit.toLong() > 0
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.done.collect {
                navController.popBackStack()
            }
        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val csvFile = requireActivity().contentResolver.openInputStream(it)
                val isr = InputStreamReader(csvFile)
                BufferedReader(isr).readLines().let { lines ->
                    viewModel.readCSV(lines)
                }
            }
        }
}