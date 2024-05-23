package com.example.gamevault.model

data class ReviewModel(
    val id: Long? = null,
    val gameId: Int,
    val userId: Int,
    val rating: Int,
    val comment: String
)
