package com.example.fittreino.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface FitTreinoApiService {
    @GET("todos")
    suspend fun getLuizEdsonTodos(): List<LuizEdsonTodoResponse>

    @GET("posts")
    suspend fun getHelderViniciusPosts(): List<HelderViniciusPostResponse>
}

data class LuizEdsonTodoResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

data class HelderViniciusPostResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    @SerializedName("body")
    val description: String
)

object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val service: FitTreinoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FitTreinoApiService::class.java)
    }
}
