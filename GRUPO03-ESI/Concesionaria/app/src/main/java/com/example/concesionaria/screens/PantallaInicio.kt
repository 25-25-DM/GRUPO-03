// Archivo: com/example/vehiculosapp/PantallaInicio.kt (modificado)
package com.example.vehiculosapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable // Importar clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add // Importar icono Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.concesionaria.R


data class Vehiculo(
    val imagenRes: Int,
    val placa: String,
    val marca: String,
    val anio: Int,
    val color: String,
    val costoPorDia: Double,
    val activo: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    vehiculos: List<Vehiculo>, // Ahora recibe la lista de vehículos
    onLogout: () -> Unit,
    onAddVehiculo: () -> Unit, // Callback para añadir nuevo vehículo
    onSelectVehiculo: (Vehiculo) -> Unit // Callback para seleccionar un vehículo para editar
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Vehículos") },
                actions = {
                    Button(onClick = { onLogout() }) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        },
        floatingActionButton = { // Añadir un FloatingActionButton para agregar
            FloatingActionButton(onClick = onAddVehiculo) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Vehículo")
            }
        }
    ) { paddingValues ->
        if (vehiculos.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No hay vehículos disponibles. ¡Añade uno!")
            }
        } else {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize().padding(8.dp)
            ) {
                items(vehiculos) { vehiculo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onSelectVehiculo(vehiculo) }, // Hacer la tarjeta clickeable
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {
                            Image(
                                painter = painterResource(id = vehiculo.imagenRes),
                                contentDescription = vehiculo.marca,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text("Placa: ${vehiculo.placa}", fontWeight = FontWeight.Bold)
                                Text("Marca: ${vehiculo.marca}")
                                Text("Año: ${vehiculo.anio}")
                                Text("Color: ${vehiculo.color}")
                                Text("Costo/día: $${vehiculo.costoPorDia}")
                                Text("Activo: ${if (vehiculo.activo) "Sí" else "No"}")
                            }
                        }
                    }
                }
            }
        }
    }
}