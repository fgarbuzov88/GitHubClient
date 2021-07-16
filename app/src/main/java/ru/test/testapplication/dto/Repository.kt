package ru.test.testapplication.dto

data class Repository(
    val id: Long,
    val name: String,
    val owner: Owner,
    val html_url: String
)

data class Owner(
    val login: String
)