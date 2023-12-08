package com.jones.my_task.core.service

import com.google.firebase.auth.FirebaseUser

interface AuthService {

    suspend fun signUp(email: String, pass: String): FirebaseUser?

    suspend fun signIn(email: String, pass: String): FirebaseUser?

    fun getCurrentUser(): FirebaseUser?

    fun logout()
}
