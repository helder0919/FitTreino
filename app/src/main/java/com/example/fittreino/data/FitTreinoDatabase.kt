package com.example.fittreino.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LuizEdsonWorkoutEntity::class, HelderViniciusMealEntity::class],
    version = 2,
    exportSchema = false
)
abstract class FitTreinoDatabase : RoomDatabase() {
    abstract fun luizEdsonWorkoutDao(): LuizEdsonWorkoutDao
    abstract fun helderViniciusMealDao(): HelderViniciusMealDao

    companion object {
        @Volatile
        private var INSTANCE: FitTreinoDatabase? = null

        fun getInstance(context: Context): FitTreinoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitTreinoDatabase::class.java,
                    "fit_treino_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
