package com.example.a22mikardex.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a22mikardex.data.CardexDatabase
import com.example.a22mikardex.data.Materia
import com.example.a22mikardex.data.MateriaRepository
import com.example.a22mikardex.data.PreferenciasAlumno
import com.example.a22mikardex.data.PromedioSemestre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardexViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = MateriaRepository(
        CardexDatabase.getDatabase(application).materiaDao()
    )
    private val preferencias = PreferenciasAlumno(application)

    // Datos del alumno (SharedPreferences) como estado observable
    private val _nombre = MutableStateFlow(preferencias.obtenerNombre())
    private val _numeroControl = MutableStateFlow(preferencias.obtenerNumeroControl())
    val nombreAlumno: StateFlow<String> = _nombre.asStateFlow()
    val numeroControl: StateFlow<String> = _numeroControl.asStateFlow()

    val alumnoRegistrado: Boolean get() = preferencias.hayAlumnoRegistrado()

    fun guardarAlumno(nombre: String, numeroControl: String) {
        preferencias.guardarAlumno(nombre, numeroControl)
        _nombre.value = nombre.trim()
        _numeroControl.value = numeroControl.trim()
    }

    // Datos de la base de datos
    val materias: StateFlow<List<Materia>> = repositorio.materias
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val promedioGeneral: StateFlow<Double?> = repositorio.promedioGeneral
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val promediosPorSemestre: StateFlow<List<PromedioSemestre>> = repositorio.promediosPorSemestre
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun agregarMateria(materia: Materia) {
        viewModelScope.launch { repositorio.insertar(materia) }
    }

    fun actualizarMateria(materia: Materia) {
        viewModelScope.launch { repositorio.actualizar(materia) }
    }

    fun eliminarMateria(materia: Materia) {
        viewModelScope.launch { repositorio.eliminar(materia) }
    }
}