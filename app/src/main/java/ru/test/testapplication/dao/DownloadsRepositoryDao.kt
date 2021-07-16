package ru.test.testapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.test.testapplication.entity.DownloadsRepositoryEntity

@Dao
interface DownloadsRepositoryDao {
    @Query("SELECT * FROM DownloadsRepositoryEntity ORDER BY owner DESC")
    fun getAllRepositories(): Flow<List<DownloadsRepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repository: DownloadsRepositoryEntity)
}