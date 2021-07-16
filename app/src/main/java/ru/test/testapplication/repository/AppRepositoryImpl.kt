package ru.test.testapplication.repository

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.ResponseBody
import ru.test.testapplication.api.AppApi
import ru.test.testapplication.dao.DownloadsRepositoryDao
import ru.test.testapplication.dao.RepositoryDao
import ru.test.testapplication.dto.Repository
import ru.test.testapplication.entity.*
import ru.test.testapplication.exceptions.ApiException
import ru.test.testapplication.exceptions.ServerException
import ru.test.testapplication.exceptions.UnknownException
import java.io.*


class AppRepositoryImpl(
    private val repositoryDao: RepositoryDao,
    private val downloadsRepositoryDao: DownloadsRepositoryDao
) : AppRepository {

    override val data = repositoryDao.getAllRepositories()
        .map(List<RepositoryEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override val downloads = downloadsRepositoryDao.getAllRepositories()
        .map(List<DownloadsRepositoryEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getUserRepositories(user: String): List<Repository> {
        try {
            val response = AppApi.service.getUserRepositories(user)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body =
                response.body() ?: throw ApiException(response.code(), response.message())
            repositoryDao.insert(body.toEntity())
            return body
        } catch (e: IOException) {
            throw ServerException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun saveUserRepository(user: String, repo: String): Repository {
        try {
            val response = AppApi.service.saveUserRepository(user, repo)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body =
                response.body() ?: throw ApiException(response.code(), response.message())
            downloadsRepositoryDao.insert(body.toDownloadsRepositoryEntity())
            return body
        } catch (e: IOException) {
            throw ServerException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun downloadUserRepository(user: String, repo: String, context: Context) {
        try {
            val response = AppApi.service.downloadUserRepository(user, repo)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }

            val body =
                response.body() ?: throw ApiException(response.code(), response.message())

            saveUserRepository(user, repo)
            writeResponseBodyToDisk(body, repo, context)
        } catch (e: IOException) {
            throw ServerException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody, repo: String, context: Context) {
        try {
            val file =
                File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), repo)
            val outputStream: OutputStream
            val fileReader = byteArrayOf(4096.toByte())
            var fileSizeDownloads = 0
            val inputStream: InputStream = body.byteStream()
            outputStream = FileOutputStream(file)

            while (true) {
                val read = inputStream.read(fileReader)

                if (read == -1) {
                    break
                }

                outputStream.write(fileReader, 0, read)
                fileSizeDownloads += read
            }
        } catch (e: IOException) {
            throw ServerException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}