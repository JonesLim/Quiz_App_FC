package com.jones.my_task.ui.screens.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jones.my_task.databinding.FragmentSignInBinding
import com.jones.my_task.ui.screens.base.BaseFragment
import com.jones.my_task.ui.screens.signin.viewModel.SignInViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    override val viewModel: SignInViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)


        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            viewModel.signIn(email, pass)
        }

        binding.tvRegister.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            navController.navigate(action)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.success.collect {
                binding.run {
                    emailEditText.text?.clear()
                    passwordEditText.text?.clear()
                }
                val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
                navController.navigate(action)
            }
        }
    }
}