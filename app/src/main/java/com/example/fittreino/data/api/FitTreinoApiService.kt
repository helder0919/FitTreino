package com.example.fittreino.data.api

import retrofit2.http.GET

interface FitTreinoApiService {
    @GET("todos")
    suspend fun getLuizEdsonTodos(): List<LuizEdsonTodoResponse>

    @GET("posts")
    suspend fun getHelderViniciusPosts(): List<HelderViniciusPostResponse>
}
