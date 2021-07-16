package ru.test.testapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.test.testapplication.dto.Owner
import ru.test.testapplication.dto.Repository

@Entity
data class DownloadsRepositoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val owner: String,
    val html_url: String
) {
    fun toDto() = Repository(
        id = id,
        name = name,
        owner = Owner(owner),
        html_url = html_url
    )
}

fun Repository.toDownloadsRepositoryEntity() = DownloadsRepositoryEntity(
    id = id,
    name = name,
    owner = owner.login,
    html_url = html_url
)

fun List<DownloadsRepositoryEntity>.toDto(): List<Repository> = map(DownloadsRepositoryEntity::toDto)