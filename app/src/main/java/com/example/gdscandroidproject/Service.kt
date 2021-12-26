package com.example.gdscandroidproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("Regional")
    fun getStatus() : Call<List<Cases>>
}