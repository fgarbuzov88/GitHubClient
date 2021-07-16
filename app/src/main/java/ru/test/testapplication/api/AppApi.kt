package ru.test.testapplication.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import ru.test.testapplication.dto.Repository
import java.util.concurrent.TimeUnit

interface AppApi {
    @GET("users/{user}/repos")
    suspend fun getUserRepositories(
        @Path("user") user: String
    ): Response<List<Repository>>

    @GET("repos/{user}/{repo}")
    suspend fun saveUserRepository(
        @Path("user") user: String,
        @Path("repo") repo: String
    ): Response<Repository>

    @Streaming
    @GET("repos/{user}/{repo}/zipball")
    suspend fun downloadUserRepository(
        @Path("user") user: String,
        @Path("repo") repo: String
    ): Response<ResponseBody>

    companion object {
        private val okhttp = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.github.com/")
            .client(okhttp)
            .build()

        val service: AppApi by lazy {
            retrofit.create(AppApi::class.java)
        }
    }
}