package com.example.concesionaria.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE fullName = :name AND password = :password LIMIT 1")
    suspend fun getUser(name: String, password: String): User?
}