package com.example.a22mikardex.data

import android.content.Context
import androidx.core.content.edit

class PreferenciasAlumno(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("cardex_prefs", Context.MODE_PRIVATE)

    fun guardarAlumno(nombre: String, numeroControl: String) {
        prefs.edit {
            putString(KEY_NOMBRE, nombre.trim())
            putString(KEY_NUM_CONTROL, numeroControl.trim())
        }
    }

    fun obtenerNombre(): String = prefs.getString(KEY_NOMBRE, "") ?: ""

    fun obtenerNumeroControl(): String = prefs.getString(KEY_NUM_CONTROL, "") ?: ""

    fun hayAlumnoRegistrado(): Boolean =
        obtenerNombre().isNotBlank() && obtenerNumeroControl().isNotBlank()

    companion object {
        private const val KEY_NOMBRE = "nombre_alumno"
        private const val KEY_NUM_CONTROL = "numero_control"
    }
}