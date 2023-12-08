package com.jones.my_task.ui.screens.signup.viewModel

interface SignUpViewModel {
    fun signUp(name: String, email: String, pass: String, confirmPass: String)

}