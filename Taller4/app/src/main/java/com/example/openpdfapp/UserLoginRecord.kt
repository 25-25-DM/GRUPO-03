package com.example.openpdfapp

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRecord(
    val username: String,
    val loginTime: String
)