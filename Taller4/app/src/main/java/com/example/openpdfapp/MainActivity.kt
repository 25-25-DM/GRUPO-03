package com.example.openpdfapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.openpdfapp.ui.theme.OpenPdfAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var hasLoggedInBefore = false // Variable para controlar si el usuario ya ha iniciado sesión antes

    private val openPdfLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            val intent = Intent(this, PdfViewerActivity::class.java).apply {
                putExtra("pdf_uri", uri.toString())
            }
            startActivity(intent)
        } ?: run {
            // Si el usuario cancela la selección del documento
            Toast.makeText(this, "Selección de PDF cancelada. Por favor, selecciona un PDF para continuar.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesManager = SharedPreferencesManager(applicationContext)
        hasLoggedInBefore = sharedPreferencesManager.getUsername() != null

        setContent {
            OpenPdfAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Siempre mostramos la LoginScreen al inicio.
                    // La lógica para decidir qué hacer después del login (primera vez o re-login)
                    // se manejará dentro de la función `onLogin` del `LoginScreen`.
                    LoginScreen { username ->
                        // Esta es la acción que ocurre cuando el usuario presiona "Ingresar"

                        // 1. Guardar el nombre de usuario y la fecha/hora
                        sharedPreferencesManager.saveUserData(username)

                        // 2. Mostrar Toast de bienvenida con la fecha/hora de ingreso
                        val loginTime = sharedPreferencesManager.getLoginTime() // Obtener el tiempo guardado para el toast
                        val welcomeMessage = if (loginTime != null) {
                            getString(R.string.welcome_message_first_time, username, loginTime)
                        } else {
                            // En teoría, loginTime no debería ser null justo después de guardarlo,
                            // pero como fallback o para depuración.
                            getString(R.string.welcome_message, username)
                        }
                        Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show()

                        // 3. Lanzar el selector de PDF
                        openPdfLauncher.launch(arrayOf("application/pdf"))

                        // Opcional: Si quieres que la pantalla de login desaparezca
                        // y el usuario no pueda volver a ella fácilmente después de un login exitoso,
                        // puedes considerar llamar a `finish()` aquí o cambiar la navegación.
                        // Sin embargo, para un taller, dejarlo así está bien para que el usuario pueda probar el flujo.
                    }

                    // *** Lógica para re-login automático (Si quieres re-activarla después) ***
                    // Si deseas que en las visitas posteriores se salte directamente a la selección del PDF
                    // sin pasar por la pantalla de login, puedes re-activar esta lógica.
                    // Por ahora, la desactivamos para asegurar el flujo de la primera pantalla siempre.
                    /*
                    if (hasLoggedInBefore) {
                        // Mostrar un Toast de bienvenida de regreso al usuario
                        val savedUsername = sharedPreferencesManager.getUsername()
                        val loginTime = sharedPreferencesManager.getLoginTime()
                        val welcomeBackMessage = if (loginTime != null && savedUsername != null) {
                            getString(R.string.welcome_back_message, savedUsername, loginTime)
                        } else {
                            getString(R.string.app_name) // Fallback
                        }
                        Toast.makeText(this, welcomeBackMessage, Toast.LENGTH_LONG).show()

                        // Lanzar el selector de PDF automáticamente al inicio de la app
                        LaunchedEffect(Unit) {
                            openPdfLauncher.launch(arrayOf("application/pdf"))
                        }
                    } else {
                        // Mostrar la pantalla de login para el primer inicio
                        LoginScreen { username ->
                            // ... (la misma lógica de guardar y lanzar PDF) ...
                        }
                    }
                    */
                }
            }
        }
    }
}