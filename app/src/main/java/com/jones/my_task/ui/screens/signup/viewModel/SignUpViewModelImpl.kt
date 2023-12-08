package com.jones.my_task.ui.screens.signup.viewModel

import androidx.lifecycle.viewModelScope
import com.jones.my_task.core.service.AuthService
import com.jones.my_task.data.model.User
import com.jones.my_task.data.repo.UserRepo
import com.jones.my_task.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepo
) : BaseViewModel(), SignUpViewModel {

    override fun signUp(name: String, email: String, pass: String, confirmPass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                _error.emit("Please fill in all the fields.")
            } else if (!isValidEmail(email)) {
                _error.emit("Invalid email format.")
            } else if (pass.length < MIN_PASSWORD_LENGTH) {
                _error.emit("Password must be at least $MIN_PASSWORD_LENGTH characters long.")
            } else if (pass != confirmPass) {
                _error.emit("Password and confirm password do not match.")
            } else {
                val user = errorHandler {
                    authService.signUp(email, pass)
                }

                if (user != null) {
                    _success.emit("Register successfully!")

                    errorHandler {
                        userRepo.addUser(
                            User(name = name, email = email)
                        )
                    }
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
