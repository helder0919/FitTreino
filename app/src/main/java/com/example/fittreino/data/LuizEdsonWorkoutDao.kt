package com.example.fittreino.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LuizEdsonWorkoutDao {
    @Query("SELECT * FROM luiz_edson_workouts ORDER BY id ASC")
    suspend fun getAll(): List<LuizEdsonWorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LuizEdsonWorkoutEntity>)

    @Query("SELECT COUNT(*) FROM luiz_edson_workouts")
    suspend fun count(): Int
}
