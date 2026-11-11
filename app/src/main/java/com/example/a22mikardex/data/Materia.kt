package com.example.a22mikardex.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materias")
data class Materia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val semestre: Int,
    val calNormal: Double? = null,
    val calRepeticion: Double? = null,
    val calEspecial: Double? = null
) {
    // Calificación que cuenta para el promedio: la más reciente capturada
    val calFinal: Double?
        get() = calEspecial ?: calRepeticion ?: calNormal
}