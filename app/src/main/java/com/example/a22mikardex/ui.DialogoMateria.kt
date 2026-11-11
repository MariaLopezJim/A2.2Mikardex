package com.example.a22mikardex.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.a22mikardex.data.Materia

private fun textoCal(valor: Double?): String =
    valor?.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() } ?: ""

// Vacía es válida (la calificación es opcional); si tiene texto debe estar entre 0 y 100
private fun calValida(texto: String): Boolean =
    texto.isBlank() || (texto.toDoubleOrNull()?.let { it in 0.0..100.0 } == true)

@Composable
fun DialogoMateria(
    materia: Materia?,
    onCancelar: () -> Unit,
    onGuardar: (Materia) -> Unit
) {
    var nombre by remember { mutableStateOf(materia?.nombre ?: "") }
    var semestre by remember { mutableStateOf(materia?.semestre?.toString() ?: "") }
    var normal by remember { mutableStateOf(textoCal(materia?.calNormal)) }
    var repeticion by remember { mutableStateOf(textoCal(materia?.calRepeticion)) }
    var especial by remember { mutableStateOf(textoCal(materia?.calEspecial)) }

    val semestreInt = semestre.toIntOrNull()
    val semestreValido = semestreInt != null && semestreInt in 1..15
    val formularioValido = nombre.isNotBlank() && semestreValido &&
            calValida(normal) && calValida(repeticion) && calValida(especial)

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (materia == null) "Agregar materia" else "Editar materia") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la materia") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = semestre,
                    onValueChange = { semestre = it },
                    label = { Text("Semestre (1 a 15)") },
                    singleLine = true,
                    isError = semestre.isNotBlank() && !semestreValido,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                CampoCalificacion("Calificación Normal", normal) { normal = it }
                CampoCalificacion("Calificación Repetición", repeticion) { repeticion = it }
                CampoCalificacion("Calificación Especial", especial) { especial = it }
            }
        },
        confirmButton = {
            TextButton(
                enabled = formularioValido,
                onClick = {
                    val s = semestreInt ?: return@TextButton
                    onGuardar(
                        Materia(
                            id = materia?.id ?: 0,
                            nombre = nombre.trim(),
                            semestre = s,
                            calNormal = normal.toDoubleOrNull(),
                            calRepeticion = repeticion.toDoubleOrNull(),
                            calEspecial = especial.toDoubleOrNull()
                        )
                    )
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}

@Composable
private fun CampoCalificacion(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text("$etiqueta (0-100)") },
        singleLine = true,
        isError = !calValida(valor),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    )
}