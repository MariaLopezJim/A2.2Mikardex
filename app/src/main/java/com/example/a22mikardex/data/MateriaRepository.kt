package com.example.a22mikardex.data

import kotlinx.coroutines.flow.Flow

class MateriaRepository(private val dao: MateriaDao) {

    val materias: Flow<List<Materia>> = dao.obtenerTodas()
    val promedioGeneral: Flow<Double?> = dao.promedioGeneral()
    val promediosPorSemestre: Flow<List<PromedioSemestre>> = dao.promedioPorSemestre()

    suspend fun insertar(materia: Materia) = dao.insertar(materia)

    suspend fun actualizar(materia: Materia) = dao.actualizar(materia)

    suspend fun eliminar(materia: Materia) = dao.eliminar(materia)
}