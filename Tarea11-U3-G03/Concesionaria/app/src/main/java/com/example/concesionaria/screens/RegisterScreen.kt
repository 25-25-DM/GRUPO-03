package com.example.concesionaria.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.concesionaria.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun validatePassword(password: String): Boolean {
    val lengthOk = password.length >= 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    return lengthOk && hasUppercase && hasDigit
}

@Composable
fun RegisterScreen(
    viewModel: UserViewModel,
    onRegisterSuccess: () -> Unit,
    onGoBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var hashedPass by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Lock
                    else
                        Icons.Filled.Lock

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar" else "Mostrar")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val passwordValid = validatePassword(pass)
                if (!passwordValid) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.",
                            duration = SnackbarDuration.Short
                        )
                    }
                } else {
                    viewModel.registerRemote(name, pass) { success, msg ->
                        scope.launch {
                            snackbarHostState.showSnackbar(msg)
                        }
                        if (success) {
                            hashedPass = viewModel.hashPassword(pass)
                            showDialog = true
                        }
                    }
                }
            }) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onGoBack) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Registro exitoso") },
                text = {
                    Column {
                        Text("Usuario creado correctamente.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Contraseña cifrada (SHA-256):\n$hashedPass")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        onRegisterSuccess()
                    }) {
                        Text("Continuar")
                    }
                }
            )
        }
    }
}

