package ru.test.testapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.test.testapplication.dao.DownloadsRepositoryDao
import ru.test.testapplication.dao.RepositoryDao
import ru.test.testapplication.entity.DownloadsRepositoryEntity
import ru.test.testapplication.entity.RepositoryEntity

@Database(
    entities = [RepositoryEntity::class, DownloadsRepositoryEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDb: RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
    abstract fun downloadsRepositoryDao(): DownloadsRepositoryDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .build()
    }
}