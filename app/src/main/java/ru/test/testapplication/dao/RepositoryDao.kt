package ru.test.testapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.test.testapplication.entity.RepositoryEntity

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM RepositoryEntity ORDER BY owner DESC")
    fun getAllRepositories(): Flow<List<RepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repositories: List<RepositoryEntity>)
}
