package com.example.a22mikardex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PantallaRegistroAlumno(
    onGuardar: (nombre: String, numeroControl: String) -> Unit,
    modifier: Modifier = Modifier,
    nombreInicial: String = "",
    numeroInicial: String = "",
    esEdicion: Boolean = false,
    onCancelar: (() -> Unit)? = null
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var numeroControl by remember { mutableStateOf(numeroInicial) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (esEdicion) "Editar mis datos" else "Bienvenido a tu Cardex",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = if (esEdicion) "Actualiza tu nombre o número de control"
            else "Captura tus datos para comenzar",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = numeroControl,
            onValueChange = { numeroControl = it },
            label = { Text("Número de control") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Button(
            onClick = { onGuardar(nombre, numeroControl) },
            enabled = nombre.isNotBlank() && numeroControl.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Guardar")
        }

        if (onCancelar != null) {
            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Cancelar")
            }
        }
    }
}