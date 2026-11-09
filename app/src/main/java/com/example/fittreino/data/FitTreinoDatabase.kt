package com.example.fittreino.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Entity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Entity(tableName = "luiz_edson_workouts")
data class LuizEdsonWorkoutEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val muscleGroup: String,
    val duration: String
)

@Entity(tableName = "helder_vinicius_meals")
data class HelderViniciusMealEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mealType: String,
    val calories: String
)

@Dao
interface LuizEdsonWorkoutDao {
    @Query("SELECT * FROM luiz_edson_workouts ORDER BY id ASC")
    suspend fun getAll(): List<LuizEdsonWorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LuizEdsonWorkoutEntity>)

    @Query("SELECT COUNT(*) FROM luiz_edson_workouts")
    suspend fun count(): Int
}

@Dao
interface HelderViniciusMealDao {
    @Query("SELECT * FROM helder_vinicius_meals ORDER BY id ASC")
    suspend fun getAll(): List<HelderViniciusMealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HelderViniciusMealEntity>)

    @Query("SELECT COUNT(*) FROM helder_vinicius_meals")
    suspend fun count(): Int
}

@Database(
    entities = [LuizEdsonWorkoutEntity::class, HelderViniciusMealEntity::class],
    version = 1,
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
