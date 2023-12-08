package com.jones.my_task.core.di

import com.jones.my_task.core.service.AuthService
import com.jones.my_task.data.repo.QuizRepo
import com.jones.my_task.data.repo.QuizRepoImpl
import com.jones.my_task.data.repo.UserRepo
import com.jones.my_task.data.repo.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Provides
    @Singleton
    fun providesUserRepo(authService: AuthService): UserRepo {
        return UserRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun provideQuizRepo(authService: AuthService): QuizRepo {
        return QuizRepoImpl(authService = authService)
    }
}