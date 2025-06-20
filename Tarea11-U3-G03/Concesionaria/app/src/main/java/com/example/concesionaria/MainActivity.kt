package com.example.concesionaria

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.amplifyframework.datastore.generated.model.User
import com.example.concesionaria.model.AppDatabase
import com.example.concesionaria.model.UserDao
import com.example.concesionaria.repository.UserRepository


import com.example.concesionaria.screens.PantallaLogin
import com.example.concesionaria.screens.RegisterScreen

import com.example.concesionaria.viewmodel.UserViewModel
import com.example.vehiculosapp.PantallaInicio


//  MainActivity navegacion sencilla
class MainActivity : ComponentActivity() {

    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getInstance(applicationContext)
        userDao = db.userDao()
        userRepository = UserRepository(userDao)
        userViewModel = UserViewModel(userRepository)

        initiate()
//        register()

        setContent {
            var screen by remember { mutableStateOf("login") }

            when (screen) {
                "login" -> PantallaLogin(
                    viewModel = userViewModel,
                    onLoginSuccess = { screen = "inicio" },
                    onGoToRegister = { screen = "register" }
                )

                "register" -> RegisterScreen(
                    viewModel = userViewModel,
                    onRegisterSuccess = { screen = "inicio" },
                    onGoBack = { screen = "login" }
                )

                "inicio" -> PantallaInicio(
                    onLogout = { screen = "login" }
                )
            }
        }
    }

    fun initiate(){
        try {
            Amplify.addPlugin(AWSDataStorePlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(applicationContext)
            Log.i("AmplifyInit", "Amplify configurado correctamente")
        } catch (e: Exception) {
            Log.e("AmplifyInit", "Error al configurar Amplify", e)
        }

    }

    fun register(){
        val model = User.builder()
            .fullName("Andres")
            .password(userViewModel.hashPassword("Cueva"))
            .build()

        Amplify.API.mutate(ModelMutation.create(model),
            { Log.i("MyAmplifyApp", "User with id: ${it.data.id}") },
            { Log.e("MyAmplifyApp", "Create failed", it) }
        )
    }
}