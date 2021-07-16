package ru.test.testapplication.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import ru.test.testapplication.dto.Repository

interface AppRepository {
    val data: Flow<List<Repository>>
    val downloads: Flow<List<Repository>>
    suspend fun getUserRepositories(user: String): List<Repository>
    suspend fun saveUserRepository(user: String, repo: String): Repository
    suspend fun downloadUserRepository(user: String, repo: String, context: Context)
}