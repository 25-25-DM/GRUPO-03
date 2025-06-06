package com.example.concesionaria.repository

import com.example.concesionaria.model.User
import com.example.concesionaria.model.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Boolean {
        return try {
            userDao.insert(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun loginUser(name: String, password: String): User? {
        return userDao.getUser(name, password)
    }
}