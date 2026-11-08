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

@Entity(tableName = "member_a_workouts")
data class MemberAWorkoutEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val muscleGroup: String,
    val duration: String
)

@Entity(tableName = "member_b_meals")
data class MemberBMealEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mealType: String,
    val calories: String
)

@Dao
interface MemberAWorkoutDao {
    @Query("SELECT * FROM member_a_workouts ORDER BY id ASC")
    suspend fun getAll(): List<MemberAWorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MemberAWorkoutEntity>)

    @Query("SELECT COUNT(*) FROM member_a_workouts")
    suspend fun count(): Int
}

@Dao
interface MemberBMealDao {
    @Query("SELECT * FROM member_b_meals ORDER BY id ASC")
    suspend fun getAll(): List<MemberBMealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MemberBMealEntity>)

    @Query("SELECT COUNT(*) FROM member_b_meals")
    suspend fun count(): Int
}

@Database(
    entities = [MemberAWorkoutEntity::class, MemberBMealEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FitTreinoDatabase : RoomDatabase() {
    abstract fun memberAWorkoutDao(): MemberAWorkoutDao
    abstract fun memberBMealDao(): MemberBMealDao

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
