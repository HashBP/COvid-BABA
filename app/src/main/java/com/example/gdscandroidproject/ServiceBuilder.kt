package com.example.gdscandroidproject

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val URL ="https://api.rootnet.in/covid19-in/stats/"
    private val okHttp = OkHttpClient.Builder()

    private val builder =Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    private val retrofit = builder.build()


    fun <T> buildService (serviceType :Class<T>):T{
        return retrofit.create(serviceType)
    }

}