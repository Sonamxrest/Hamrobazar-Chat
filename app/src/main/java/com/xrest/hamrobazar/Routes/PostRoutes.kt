package com.xrest.hamrobazar.Routes

import com.xrest.hamrobazar.Response.CommonResponse
import com.xrest.hamrobazar.Response.PostResponse
import com.xrest.hamrobazar.ResponseClass.Comments
import com.xrest.hamrobazar.ResponseClass.Post
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PostRoutes {

    @POST("/post/product")
    suspend fun addPost(@Header("Authorization") token:String,@Body post:Post): Response<CommonResponse>
    @Multipart
    @PUT("/update/productImage/{id}")
    suspend fun updatePimage(@Path("id") id:String,@Part body:MultipartBody.Part):Response<CommonResponse>

    @GET("/get/product")
    suspend fun getProducts():Response<PostResponse>
    @PUT("/like/{id}")
    suspend fun likeProduct(@Header("Authorization") token:String, @Path("id") id:String):Response<CommonResponse>
    @PUT("/comment")
    suspend fun comment(@Header("Authorization") token:String,@Body com:Comments):Response<CommonResponse>

    @PUT("/update/product/{id}")
        suspend fun updateProduct(@Path("id") id:String,@Body post:Post):Response<CommonResponse>
    @PUT("/sold/{id}")
    suspend fun sold(@Path("id") id:String):Response<CommonResponse>
        @DELETE
        suspend fun delete(@Path("id") id:String):Response<CommonResponse>
        @GET("/person/post/{id}")
        suspend fun getPP(@Path("id")token:String):Response<PostResponse>
}