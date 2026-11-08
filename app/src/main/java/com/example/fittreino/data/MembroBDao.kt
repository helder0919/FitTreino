package com.example.fittreino.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MembroBDao {
    @Query("SELECT * FROM membro_b_table")
    fun getAll(): Flow<List<MembroBEntity>>

    @Insert
    suspend fun insert(item: MembroBEntity)
}
