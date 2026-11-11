package com.example.a22mikardex.data



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Resultado de la consulta de promedio por semestre
data class PromedioSemestre(
    val semestre: Int,
    val promedio: Double?
)

@Dao
interface MateriaDao {

    @Insert
    suspend fun insertar(materia: Materia)

    @Update
    suspend fun actualizar(materia: Materia)

    @Delete
    suspend fun eliminar(materia: Materia)

    @Query("SELECT * FROM materias ORDER BY semestre, nombre")
    fun obtenerTodas(): Flow<List<Materia>>

    @Query("SELECT * FROM materias WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Materia?

    // Promedio general usando la última calificación capturada
    @Query("""
        SELECT AVG(COALESCE(calEspecial, calRepeticion, calNormal))
        FROM materias
    """)
    fun promedioGeneral(): Flow<Double?>

    // Promedio agrupado por semestre
    @Query("""
        SELECT semestre,
               AVG(COALESCE(calEspecial, calRepeticion, calNormal)) AS promedio
        FROM materias
        GROUP BY semestre
        ORDER BY semestre
    """)
    fun promedioPorSemestre(): Flow<List<PromedioSemestre>>
}