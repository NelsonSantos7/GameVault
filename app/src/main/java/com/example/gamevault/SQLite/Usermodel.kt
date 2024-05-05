package com.example.gamevault.model

data class Usermodel(
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val salt: String? = null

)
