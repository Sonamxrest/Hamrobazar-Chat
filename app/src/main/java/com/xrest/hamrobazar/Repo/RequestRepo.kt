package com.xrest.hamrobazar.Repo

import com.xrest.hamrobazar.Response.CommonResponse
import com.xrest.hamrobazar.Response.RequestResponse
import com.xrest.hamrobazar.ResponseClass.Request
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.Retrofit.apiRequest
import com.xrest.hamrobazar.Routes.RequestRoutes

class RequestRepo:apiRequest() {
    val api = RetrofitBuilder.buildApiRequest(RequestRoutes::class.java)

    suspend fun  sendRequest(id:String):CommonResponse{
        return handelApiRequest {
            api.sendRequest(RetrofitBuilder.token!!,id)
        }

    }
    suspend fun acceptReq(data:Request):CommonResponse{
        return handelApiRequest {
            api.acceptRequest(RetrofitBuilder.token!!,data)
        }
    }

    suspend fun getRequest():RequestResponse{
        return handelApiRequest {
            api.getRequest(RetrofitBuilder.token!!)
        }
    }
    suspend fun deleteRequest(data:String):CommonResponse{
        return handelApiRequest {
            api.deleteReq(data)
        }
    }


}