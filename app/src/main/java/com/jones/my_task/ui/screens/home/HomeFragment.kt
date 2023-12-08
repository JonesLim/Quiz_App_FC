package com.jones.my_task.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jones.my_task.databinding.FragmentHomeBinding
import com.jones.my_task.ui.adapter.QuizAdapter
import com.jones.my_task.ui.screens.base.BaseFragment
import com.jones.my_task.ui.screens.home.viewModel.HomeViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModel: HomeViewModelImpl by viewModels()
    private lateinit var quizAdapter: QuizAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        super.onViewCreated(view, savedInstanceState)

        viewModel.getQuizzes()
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        setupRecyclerView()

        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddQuizFragment()
            navController.navigate(action)
        }



        binding.fabLogout.setOnClickListener {
            viewModel.logout()

            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout Confirmation")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                viewModel.logout()

                Toast.makeText(requireContext(), "Logout successful!", Toast.LENGTH_SHORT).show()

                val action = HomeFragmentDirections.actionHomeFragmentToSignInFragment()
                navController.navigate(action)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter(
            userId = viewModel.currentUser?.uid ?: "",
            emptyList(),
            { quizId -> showDeleteQuizConfirmationDialog(quizId) },
            { quizId ->
                val action = HomeFragmentDirections.actionHomeFragmentToQuizFragment(quizId)
                navController.navigate(action)
            }
        )
        binding.rvQuiz.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = quizAdapter
        }
    }


    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.quizzes.collect {
                quizAdapter.setQuizs(it)
            }
        }
    }

    private fun showDeleteQuizConfirmationDialog(quizId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Quiz Confirmation")
            .setMessage("Are you sure you want to delete this quiz?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteQuiz(quizId)

                Toast.makeText(requireContext(), "Quiz deleted successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}
