package com.example.concesionaria.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.vehiculosapp.Vehiculo // Asegúrate de que esta importación sea correcta si Vehiculo está en otro paquete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGestionVehiculo(
    vehiculoParaEditar: Vehiculo? = null, // Null para insertar, Vehiculo para editar
    onSave: (Vehiculo) -> Unit,
    onDelete: (Vehiculo) -> Unit, // Solo para edición
    onCancel: () -> Unit
) {
    // Estados para los campos del formulario
    var placa by remember { mutableStateOf(vehiculoParaEditar?.placa ?: "") }
    var marca by remember { mutableStateOf(vehiculoParaEditar?.marca ?: "") }
    var anio by remember { mutableStateOf(vehiculoParaEditar?.anio?.toString() ?: "") }
    var color by remember { mutableStateOf(vehiculoParaEditar?.color ?: "") }
    var costoPorDia by remember { mutableStateOf(vehiculoParaEditar?.costoPorDia?.toString() ?: "") }
    var activo by remember { mutableStateOf(vehiculoParaEditar?.activo ?: true) }

    val isEditing = vehiculoParaEditar != null
    val title = if (isEditing) "Editar Vehículo" else "Nuevo Vehículo"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = placa,
                onValueChange = { placa = it },
                label = { Text("Placa") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = anio,
                onValueChange = { anio = it },
                label = { Text("Año") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Color") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = costoPorDia,
                onValueChange = { costoPorDia = it },
                label = { Text("Costo por Día") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Activo:")
                Checkbox(
                    checked = activo,
                    onCheckedChange = { activo = it }
                )
            }

            Spacer(Modifier.height(20.dp))

            // Botones de acción
            Button(
                onClick = {
                    val nuevoVehiculo = Vehiculo(
                        // R.drawable.auto1 es un placeholder, en un caso real se manejaría la imagen
                        imagenRes = vehiculoParaEditar?.imagenRes ?: com.example.concesionaria.R.drawable.auto1,
                        placa = placa,
                        marca = marca,
                        anio = anio.toIntOrNull() ?: 0,
                        color = color,
                        costoPorDia = costoPorDia.toDoubleOrNull() ?: 0.0,
                        activo = activo
                    )
                    onSave(nuevoVehiculo)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            if (isEditing) {
                Button(
                    onClick = { vehiculoParaEditar?.let { onDelete(it) } },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar")
                }
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}