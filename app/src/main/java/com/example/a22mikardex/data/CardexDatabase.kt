package com.example.a22mikardex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Materia::class], version = 1, exportSchema = false)
abstract class CardexDatabase : RoomDatabase() {

    abstract fun materiaDao(): MateriaDao

    companion object {
        @Volatile
        private var INSTANCE: CardexDatabase? = null

        fun getDatabase(context: Context): CardexDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CardexDatabase::class.java,
                    "cardex_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}