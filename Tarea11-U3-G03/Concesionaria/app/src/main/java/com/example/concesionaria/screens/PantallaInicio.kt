package com.example.vehiculosapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun PantallaInicio(onLogout: () -> Unit) {
    val vehiculos = listOf(
        Vehiculo(R.drawable.auto1, "ABC123", "Toyota", 2020, "Rojo", 50.0, true, ),
        Vehiculo(R.drawable.auto2, "DEF456", "Chevrolet", 2018, "Azul", 40.0, false),
        Vehiculo(R.drawable.auto3, "GHI789", "Ford", 2022, "Negro", 60.0, true)
    )

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
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            items(vehiculos) { vehiculo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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
