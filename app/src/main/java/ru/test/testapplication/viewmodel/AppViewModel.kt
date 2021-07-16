package ru.test.testapplication.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.test.testapplication.R
import ru.test.testapplication.db.AppDb
import ru.test.testapplication.dto.Repository
import ru.test.testapplication.exceptions.AppException
import ru.test.testapplication.repository.AppRepository
import ru.test.testapplication.repository.AppRepositoryImpl
import ru.test.testapplication.utils.SingleLiveEvent

class AppViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: AppRepository =
        AppRepositoryImpl(
            AppDb.getInstance(context = application).repositoryDao(),
            AppDb.getInstance(context = application).downloadsRepositoryDao()
        )

    val data: Flow<List<Repository>> =
        repository.data
            .catch { e ->
                AppException.from(e)
                _loadRepositoryExceptionEvent.call()
            }

    val downloads: Flow<List<Repository>> =
        repository.downloads
            .catch { e ->
                AppException.from(e)
                _loadRepositoryExceptionEvent.call()
            }

    private val _loadRepositoryExceptionEvent = SingleLiveEvent<Unit>()
    val loadRepositoryExceptionEvent: LiveData<Unit>
        get() = _loadRepositoryExceptionEvent

    fun getUserRepositories(user: String) =
        viewModelScope.launch {
            try {
                repository.getUserRepositories(user)
            } catch (e: Exception) {
                e.printStackTrace()
                _loadRepositoryExceptionEvent.call()
            }
        }

    fun downloadUserRepository(user: String, repo: String, context: Context) =
        viewModelScope.launch {
            try {
                repository.downloadUserRepository(user, repo, context)
                Toast.makeText(getApplication(), R.string.successful_download, Toast.LENGTH_LONG)
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()
                _loadRepositoryExceptionEvent.call()
            }
        }
}