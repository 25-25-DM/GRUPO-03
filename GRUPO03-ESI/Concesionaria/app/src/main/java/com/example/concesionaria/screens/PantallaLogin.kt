package com.example.concesionaria.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.concesionaria.R
import com.example.concesionaria.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Result.Companion.success

@Composable
fun PantallaLogin(
    viewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var plainPass by remember { mutableStateOf("") }
    var hashedPass by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.onixlogo),
                contentDescription = "Logo Onix",
                modifier = Modifier.size(120.dp)
            )

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre Apellido") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (viewModel.loginFailed) {
                Text(
                    text = "Credenciales incorrectas",
                    color = Color.Red
                )
            }

            Button(
                onClick = {
                    if (name.isNotBlank() && pass.isNotBlank()) {
                        viewModel.loginAsync(name, pass) {
                            plainPass = pass
                            hashedPass = viewModel.hashPassword(pass)
                            showDialog = true // Muestra el diálogo
                        }
                    } else {
                        viewModel.updateLoginFailedStatus(true)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            TextButton(onClick = onGoToRegister) {
                Text("¿No tienes cuenta? Regístrate")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Inicio de sesión exitoso") },
                    text = {
                        Column {
                            Text("Contraseña original: $plainPass")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Contraseña cifrada (SHA-256):\n$hashedPass")
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            onLoginSuccess() // Navega al inicio
                        }) {
                            Text("Continuar")
                        }
                    }
                )
            }
        }
    }
}