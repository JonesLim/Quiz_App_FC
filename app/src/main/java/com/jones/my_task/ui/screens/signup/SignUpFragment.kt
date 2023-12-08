package com.jones.my_task.ui.screens.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jones.my_task.databinding.FragmentSignUpBinding
import com.jones.my_task.ui.screens.base.BaseFragment
import com.jones.my_task.ui.screens.signup.viewModel.SignUpViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    override val viewModel: SignUpViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        binding.run {
            signUpButton.setOnClickListener {
                viewModel.signUp(
                    nameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    passwordEditText2.text.toString()
                )
            }

            tvSigIn.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.success.collect {
                binding.run {
                    nameEditText.text?.clear()
                    emailEditText.text?.clear()
                    passwordEditText.text?.clear()
                    passwordEditText2.text?.clear()
                }
                val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                navController.navigate(action)
            }
        }
    }
}