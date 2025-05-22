package com.example.concesionaria.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionaria.model.AppDatabase
import com.example.concesionaria.model.User
import com.example.concesionaria.repository.UserRepository
import kotlinx.coroutines.launch
import java.security.MessageDigest

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).userDao()

    var isLoggedIn by mutableStateOf(false)
        private set

    var registerSuccess by mutableStateOf<Boolean?>(null)
        private set

    var loginFailed by mutableStateOf(false)
        private set

    fun registerAsync(fullName: String, password: String) {
        viewModelScope.launch {
            try {
                dao.insert(User(fullName = fullName, password = password))
                registerSuccess = true
            } catch (e: Exception) {
                registerSuccess = false
            }
        }
    }

    fun loginAsync(fullName: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = dao.getUser(fullName, password)
            isLoggedIn = user != null
            loginFailed = user == null
            if (isLoggedIn) onSuccess()
        }
    }


    suspend fun register(fullName: String, password: String): Boolean {
        return try {
            dao.insert(User(fullName = fullName, password = password))
            true
        } catch (e: Exception) {
            false
        }
    }

    fun updateLoginFailedStatus(failed: Boolean) {
        loginFailed = failed
    }fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

}