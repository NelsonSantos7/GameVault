package com.example.gamevault.model

data class ReviewModel(
    var id: String? = null,
    val gameId: String,
    val userId: String,
    val rating: Int,
    val comment: String
)
