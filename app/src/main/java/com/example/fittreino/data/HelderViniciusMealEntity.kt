package com.example.fittreino.data

import androidx.room.Entity

@Entity(tableName = "helder_vinicius_meals")
data class HelderViniciusMealEntity(
    @androidx.room.PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mealType: String,
    val calories: String
)
