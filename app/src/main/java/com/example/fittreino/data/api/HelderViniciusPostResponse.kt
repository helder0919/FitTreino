package com.example.fittreino.data.api

import com.google.gson.annotations.SerializedName

data class HelderViniciusPostResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    @SerializedName("body")
    val description: String
)
