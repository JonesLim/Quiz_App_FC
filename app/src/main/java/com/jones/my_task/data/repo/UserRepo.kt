package com.jones.my_task.data.repo

import com.jones.my_task.data.model.User

interface UserRepo {
    suspend fun addUser(user: User)
    suspend fun getUser(): User?
    suspend fun removeUser()
    suspend fun updateUser(user: User)
}