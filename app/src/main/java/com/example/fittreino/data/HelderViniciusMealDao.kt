package com.example.fittreino.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HelderViniciusMealDao {
    @Query("SELECT * FROM helder_vinicius_meals ORDER BY id ASC")
    suspend fun getAll(): List<HelderViniciusMealEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HelderViniciusMealEntity>)

    @Query("SELECT COUNT(*) FROM helder_vinicius_meals")
    suspend fun count(): Int
}
