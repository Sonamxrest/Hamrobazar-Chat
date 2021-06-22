package com.xrest.hamrobazar.Retrofit

import com.xrest.hamrobazar.ResponseClass.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {

     val BASE_URL= "http://192.168.0.106:3000/"
    var token:String?=null
    var user: User?=null
    val builder = OkHttpClient.Builder()
    val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
    val retrifit = retrofitBuilder.build()
    fun<T> buildApiRequest(service:Class<T>):T{
        return  retrifit.create(service)

    }





}