package com.example.a22mikardex.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a22mikardex.data.Materia

private const val MINIMA_APROBATORIA = 70.0

private fun fmt(valor: Double?): String =
    valor?.let { if (it % 1.0 == 0.0) it.toInt().toString() else "%.1f".format(it) } ?: "—"

@Composable
fun PantallaCardex(
    viewModel: CardexViewModel,
    onVerPromedios: () -> Unit,
    onEditarAlumno: () -> Unit,
    modifier: Modifier = Modifier
) {
    val materias by viewModel.materias.collectAsStateWithLifecycle()
    val nombre by viewModel.nombreAlumno.collectAsStateWithLifecycle()
    val numeroControl by viewModel.numeroControl.collectAsStateWithLifecycle()

    var mostrarDialogo by remember { mutableStateOf(false) }
    var materiaEditando by remember { mutableStateOf<Materia?>(null) }
    var materiaPorEliminar by remember { mutableStateOf<Materia?>(null) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                materiaEditando = null
                mostrarDialogo = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar materia")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = nombre, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "No. de control: $numeroControl",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(onClick = onEditarAlumno) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar mis datos")
                }
            }

            OutlinedButton(
                onClick = onVerPromedios,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Ver promedios")
            }

            if (materias.isEmpty()) {
                Text(
                    text = "Aún no has capturado materias.\nToca + para agregar la primera.",
                    modifier = Modifier.padding(top = 32.dp)
                )
            } else {
                val porSemestre = materias.groupBy { it.semestre }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    porSemestre.forEach { (semestre, lista) ->
                        item(key = "sem_$semestre") {
                            Text(
                                text = "Semestre $semestre",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                        items(lista, key = { it.id }) { materia ->
                            TarjetaMateria(
                                materia = materia,
                                onClick = {
                                    materiaEditando = materia
                                    mostrarDialogo = true
                                },
                                onEliminar = { materiaPorEliminar = materia }
                            )
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoMateria(
            materia = materiaEditando,
            onCancelar = { mostrarDialogo = false },
            onGuardar = { materia ->
                if (materiaEditando == null) viewModel.agregarMateria(materia)
                else viewModel.actualizarMateria(materia)
                mostrarDialogo = false
            }
        )
    }

    materiaPorEliminar?.let { materia ->
        AlertDialog(
            onDismissRequest = { materiaPorEliminar = null },
            title = { Text("Eliminar materia") },
            text = { Text("¿Seguro que quieres eliminar \"${materia.nombre}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarMateria(materia)
                    materiaPorEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { materiaPorEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun TarjetaMateria(
    materia: Materia,
    onClick: () -> Unit,
    onEliminar: () -> Unit
) {
    val final = materia.calFinal
    val reprobada = final != null && final < MINIMA_APROBATORIA

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = if (reprobada) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(materia.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Normal: ${fmt(materia.calNormal)}  ·  " +
                            "Repetición: ${fmt(materia.calRepeticion)}  ·  " +
                            "Especial: ${fmt(materia.calEspecial)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = when {
                        final == null -> "Sin calificación"
                        reprobada -> "Final: ${fmt(final)} (reprobada)"
                        else -> "Final: ${fmt(final)}"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = if (reprobada) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}