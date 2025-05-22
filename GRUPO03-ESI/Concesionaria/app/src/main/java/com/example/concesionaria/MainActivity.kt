// Archivo: com/example/concesionaria/MainActivity.kt (modificado)
package com.example.concesionaria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf // Importar para lista observable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.concesionaria.screens.PantallaGestionVehiculo // Importar la nueva pantalla
import com.example.concesionaria.screens.PantallaLogin
import com.example.concesionaria.screens.RegisterScreen
import com.example.concesionaria.ui.theme.ConcesionariaTheme
import com.example.concesionaria.viewmodel.UserViewModel
import com.example.vehiculosapp.PantallaInicio
import com.example.vehiculosapp.Vehiculo // Importar la data class Vehiculo

// 6. MainActivity con navegación simple y gestión de vehículos
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConcesionariaTheme { // Envuelve tu contenido con el tema
                val userViewModel: UserViewModel = viewModel(factory = object : ViewModelProvider.AndroidViewModelFactory(application) {})
                var currentScreen by remember { mutableStateOf("login") } // Estado para la pantalla actual
                var selectedVehiculo by remember { mutableStateOf<Vehiculo?>(null) } // Estado para el vehículo seleccionado en edición

                // Lista de vehículos en memoria (simulando un repositorio/DB)
                val vehiculos = remember {
                    mutableStateListOf(
                        Vehiculo(R.drawable.auto1, "ABC123", "Toyota", 2020, "Rojo", 50.0, true),
                        Vehiculo(R.drawable.auto2, "DEF456", "Chevrolet", 2018, "Azul", 40.0, false),
                        Vehiculo(R.drawable.auto3, "GHI789", "Ford", 2022, "Negro", 60.0, true)
                    )
                }

                when (currentScreen) {
                    "login" -> PantallaLogin(
                        userViewModel,
                        onLoginSuccess = { currentScreen = "inicio" },
                        onGoToRegister = { currentScreen = "register" }
                    )
                    "register" -> RegisterScreen(
                        userViewModel,
                        onGoToLogin = { currentScreen = "login" }
                    )
                    "inicio" -> PantallaInicio(
                        vehiculos = vehiculos, // Pasamos la lista de vehículos
                        onLogout = { currentScreen = "login" },
                        onAddVehiculo = {
                            selectedVehiculo = null // Asegurarse de que no haya vehículo seleccionado para añadir
                            currentScreen = "manage_vehicle" // Navegar a la pantalla de gestión
                        },
                        onSelectVehiculo = { vehiculo ->
                            selectedVehiculo = vehiculo // Guardar el vehículo seleccionado para editar
                            currentScreen = "manage_vehicle" // Navegar a la pantalla de gestión
                        }
                    )
                    "manage_vehicle" -> PantallaGestionVehiculo(
                        vehiculoParaEditar = selectedVehiculo, // Pasamos el vehículo si es para editar
                        onSave = { updatedVehiculo ->
                            if (selectedVehiculo == null) { // Si es nuevo vehículo
                                vehiculos.add(updatedVehiculo)
                            } else { // Si es edición
                                val index = vehiculos.indexOfFirst { it.placa == selectedVehiculo?.placa }
                                if (index != -1) {
                                    vehiculos[index] = updatedVehiculo // Actualizar el vehículo en la lista
                                }
                            }
                            selectedVehiculo = null // Limpiar selección
                            currentScreen = "inicio" // Volver a la pantalla de inicio
                        },
                        onDelete = { vehiculoToDelete ->
                            vehiculos.remove(vehiculoToDelete) // Eliminar el vehículo
                            selectedVehiculo = null // Limpiar selección
                            currentScreen = "inicio" // Volver a la pantalla de inicio
                        },
                        onCancel = {
                            selectedVehiculo = null // Limpiar selección
                            currentScreen = "inicio" // Volver a la pantalla de inicio
                        }
                    )
                }
            }
        }
    }
}