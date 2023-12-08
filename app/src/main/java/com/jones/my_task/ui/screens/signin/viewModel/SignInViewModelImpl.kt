package com.jones.my_task.ui.screens.signin.viewModel

import androidx.lifecycle.viewModelScope
import com.jones.my_task.core.service.AuthService
import com.jones.my_task.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModelImpl @Inject constructor(
    private val authService: AuthService
) : BaseViewModel(), SignInViewModel {

    override fun signIn(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (email.isEmpty() || pass.isEmpty()) {
                _error.emit("Please enter both email and password.")
            } else if (!isValidEmail(email)) {
                _error.emit("Invalid email format.")
            } else if (pass.length < MIN_PASSWORD_LENGTH) {
                _error.emit("Password must be at least $MIN_PASSWORD_LENGTH characters long.")
            } else {
                val result = errorHandler {
                    authService.signIn(email, pass)
                }
                if (result != null) {
                    _success.emit("Login successfully!")
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }
}
