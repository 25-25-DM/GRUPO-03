package com.example.concesionaria.viewmodel

import com.amplifyframework.datastore.generated.model.User as AmplifyUser
import com.example.concesionaria.model.User as LocalUser
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.concesionaria.repository.UserRepository

import kotlinx.coroutines.launch
import java.security.MessageDigest

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun loginAsync(
        name: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val hashedPassword = hashPassword(password)

        viewModelScope.launch {
            val remoteUser = repository.loginRemoteUser(name, hashedPassword)
            if (remoteUser != null) {
                repository.syncUserToLocal(remoteUser)
                onSuccess()
            } else {
                val localUser = repository.loginLocalUser(name, hashedPassword)
                if (localUser != null) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }
        }
    }

    fun registerRemote(
        name: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {

        if (!isPasswordSecure(password)) {
            onResult(false, "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.")
            return
        }

        val hashedPassword = hashPassword(password)

        viewModelScope.launch {
            val success = repository.registerRemoteUser(name, hashedPassword)
            if (success) {
                val amplifyUser = AmplifyUser.builder()
                    .fullName(name)
                    .password(hashedPassword)
                    .build()
                repository.syncUserToLocal(amplifyUser)
            }
            onResult(success, if (success) "Usuario registrado correctamente" else "Error al registrar")
        }
    }

    fun registerLocal(
        name: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {

        if (!isPasswordSecure(password)) {
            onResult(false, "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.")
            return
        }
        val hashedPassword = hashPassword(password)

        viewModelScope.launch {
            val user = LocalUser(fullName = name, password = hashedPassword)
            val result = repository.registerLocalUser(user)
            onResult(result, if (result) "Usuario registrado localmente" else "Error al registrar")
        }
    }

    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun isPasswordSecure(password: String): Boolean {
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasMinLength = password.length >= 8
        return hasUppercase && hasDigit && hasMinLength
    }



}