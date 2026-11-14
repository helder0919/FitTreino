package com.example.fittreino.data

import androidx.room.Entity

@Entity(tableName = "luiz_edson_workouts")
data class LuizEdsonWorkoutEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val muscleGroup: String,
    val duration: String
)
