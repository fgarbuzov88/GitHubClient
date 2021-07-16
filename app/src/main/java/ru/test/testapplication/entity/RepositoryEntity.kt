package ru.test.testapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.test.testapplication.dto.Owner
import ru.test.testapplication.dto.Repository

@Entity
data class RepositoryEntity(
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

fun Repository.toEntity() = RepositoryEntity(
    id = id,
    name = name,
    owner = owner.login,
    html_url = html_url
)

fun List<RepositoryEntity>.toDto(): List<Repository> = map(RepositoryEntity::toDto)
fun List<Repository>.toEntity(): List<RepositoryEntity> = map(Repository::toEntity)