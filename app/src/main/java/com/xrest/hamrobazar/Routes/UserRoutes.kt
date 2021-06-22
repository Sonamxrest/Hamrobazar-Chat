package com.xrest.hamrobazar.Routes

import com.xrest.hamrobazar.Response.CommonResponse
import com.xrest.hamrobazar.Response.LoginResponse
import com.xrest.hamrobazar.Response.UserResponse
import com.xrest.hamrobazar.Response.UserResponses
import com.xrest.hamrobazar.ResponseClass.User
import com.xrest.hamrobazar.ResponseClass.Users
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserRoutes {

@FormUrlEncoded
    @POST("/login")
    suspend fun login(@Field("Username") username:String, @Field("Password") password:String):Response<LoginResponse>
    @POST("/insert/user")
    suspend fun register(@Body user: User):Response<UserResponse>

    @Multipart
    @PUT("/update/profile/{id}")
    suspend fun updateProfile(@Path("id") id:String,@Part part:MultipartBody.Part):Response<UserResponse>

    @PUT("/update/user")
    suspend fun updateUser(@Body user: Users, @Header("Authorization") token:String):Response<CommonResponse>

    @GET("/user")
    suspend fun getUser(@Header("Authorization") token:String):Response<CommonResponse>

    @PUT("/logout")
    suspend fun logout(@Header("Authorization") token:String):Response<CommonResponse>


    @GET("/showFriends")
    suspend fun showFriends(@Header("Authorization") token:String):Response<UserResponse>
    @GET("/all")
    suspend fun All():Response<UserResponses>
}