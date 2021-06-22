package com.xrest.hamrobazar.Routes

import com.xrest.hamrobazar.Response.CommonResponse
import com.xrest.hamrobazar.Response.RequestResponse
import com.xrest.hamrobazar.ResponseClass.Message
import com.xrest.hamrobazar.ResponseClass.Request
import retrofit2.Response
import retrofit2.http.*

interface RequestRoutes {


@POST("/sendRequest/{id}")
suspend fun sendRequest(@Header("Authorization")token:String,@Path("id") id:String): Response<CommonResponse>
@PUT("/acceptRequest")
suspend fun acceptRequest(@Header("Authorization")token:String,@Body message: Request):Response<CommonResponse>
@GET("/showRequest")
suspend fun getRequest(@Header("Authorization")token:String):Response<RequestResponse>
@DELETE("/deleteRequest/{id}")
suspend fun  deleteReq( @Path("id")data:String):Response<CommonResponse>

}