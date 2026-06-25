package com.example.frontend.data.remote.dto

data class SignInRequest(
    val email: String,
    val password: String
)

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String
)

data class GoogleAuthRequest(
    val name: String,
    val email: String,
    val photo: String
)
