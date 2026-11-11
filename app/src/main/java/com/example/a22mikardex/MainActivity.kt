package com.example.a22mikardex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a22mikardex.ui.CardexViewModel
import com.example.a22mikardex.ui.PantallaCardex
import com.example.a22mikardex.ui.PantallaPromedios
import com.example.a22mikardex.ui.PantallaRegistroAlumno
import com.example.a22mikardex.ui.theme.A22MikardexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            A22MikardexTheme {
                val viewModel: CardexViewModel = viewModel()
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (viewModel.alumnoRegistrado) "cardex" else "registro"
                ) {
                    // Primera vez: captura inicial
                    composable("registro") {
                        PantallaRegistroAlumno(
                            onGuardar = { nombre, numControl ->
                                viewModel.guardarAlumno(nombre, numControl)
                                navController.navigate("cardex") {
                                    popUpTo("registro") { inclusive = true }
                                }
                            },
                            modifier = Modifier.systemBarsPadding()
                        )
                    }

                    composable("cardex") {
                        PantallaCardex(
                            viewModel = viewModel,
                            onVerPromedios = { navController.navigate("promedios") },
                            onEditarAlumno = { navController.navigate("perfil") }
                        )
                    }

                    composable("promedios") {
                        PantallaPromedios(
                            viewModel = viewModel,
                            onVolver = { navController.popBackStack() }
                        )
                    }

                    // Editar nombre y número de control
                    composable("perfil") {
                        PantallaRegistroAlumno(
                            nombreInicial = viewModel.nombreAlumno.value,
                            numeroInicial = viewModel.numeroControl.value,
                            esEdicion = true,
                            onCancelar = { navController.popBackStack() },
                            onGuardar = { nombre, numControl ->
                                viewModel.guardarAlumno(nombre, numControl)
                                navController.popBackStack()
                            },
                            modifier = Modifier.systemBarsPadding()
                        )
                    }
                }
            }
        }
    }
}